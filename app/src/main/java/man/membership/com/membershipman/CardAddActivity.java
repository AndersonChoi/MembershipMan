package man.membership.com.membershipman;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.membership.card.dao.Card;
import com.membership.card.dao.CardDao;
import com.membership.card.dao.DaoMaster;
import com.membership.card.dao.DaoSession;

import org.w3c.dom.Text;

/**
 * Created by HackerAnderson on 16. 9. 10..
 */
public class CardAddActivity extends Activity {


    private  int CARD_BACKGROUND_NUMBER = -1;
    private  String CARD_NAME = "";
    private  String CARD_NUMBER = "";
    private String CARD_TYPE="";

    EditText addCardTitle, addCardBarcode;
    ImageView addCardBarcodeImage;
    TextView addCardBarcodeText;
    Button closeButton, closeButton2;
    Button addButton, addFinalButton;
    RelativeLayout beforeLayout, afterLayout;

    ImageView cardBackgroundSelectImageView;

    TextView finalTitle;
    ImageView finalBarcode;

    private Vibrator vibe;

    private DisplayMetrics mMetrics;
    private GridView gridview;

    private static String finalCardTitle, finalCardBarcode;


    private DaoMaster.DevOpenHelper helper = null;
    private SQLiteDatabase db = null;
    private DaoMaster daoMaster = null;
    private DaoSession daoSession = null;
    private CardDao cardDao = null;
    private Cursor cursor = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivty_add_card);


        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(CardAddActivity.this, "card.db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        cardDao = daoSession.getCardDao();


        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        beforeLayout = (RelativeLayout) findViewById(R.id.add_card_before_layout);
        afterLayout = (RelativeLayout) findViewById(R.id.add_card_after_layout);
        finalTitle = (TextView) findViewById(R.id.add_card_title_text);
        finalBarcode = (ImageView) findViewById(R.id.add_card_barcode);

        cardBackgroundSelectImageView = (ImageView) findViewById(R.id.add_card_background);


        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));
        gridview.setOnItemClickListener(gridviewOnItemClickListener);


        closeButton = (Button) findViewById(R.id.add_card_close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                vibe.vibrate(30);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent i = getIntent();
                        i.putExtra("CARD", "NO");
                        setResult(RESULT_OK, i);

                        finish();
                    }
                }, 30);
            }
        });
        closeButton2 = (Button) findViewById(R.id.add_card_close_button2);
        closeButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                vibe.vibrate(30);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent i = getIntent();
                        i.putExtra("CARD", "NO");
                        setResult(RESULT_OK, i);

                        finish();
                    }
                }, 30);
            }
        });

        addButton = (Button) findViewById(R.id.add_card_add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                if (addCardTitle.getText().toString().length() != 0 && addCardBarcode.getText().toString().length() != 0) {

                    gridview.setVisibility(View.VISIBLE);


                    beforeLayout.setVisibility(View.GONE);
                    beforeLayout.animate()
                            .alpha(0f)
                            .setInterpolator(AnimationUtils.loadInterpolator(CardAddActivity.this, android.R.anim.decelerate_interpolator))
                            .setDuration(300)
                            .start();


                    afterLayout.setVisibility(View.VISIBLE);
                    afterLayout.animate()
                            .alpha(0f)
                            .setInterpolator(AnimationUtils.loadInterpolator(CardAddActivity.this, android.R.anim.decelerate_interpolator))
                            .setDuration(0)
                            .start();

                    afterLayout.animate()
                            .alpha(1.0f)
                            .setInterpolator(AnimationUtils.loadInterpolator(CardAddActivity.this, android.R.anim.decelerate_interpolator))
                            .setDuration(500)
                            .start();

                    vibe.vibrate(22);
                } else {
                    Animation shake = AnimationUtils.loadAnimation(CardAddActivity.this, R.anim.shake);
                    beforeLayout.startAnimation(shake);
                    vibe.vibrate(400);

                }
            }
        });

        addFinalButton = (Button) findViewById(R.id.add_card_add_button_final);
        addFinalButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                Log.e("","CARD_BACKGROUND_NUMBER:"+CARD_BACKGROUND_NUMBER);
                Log.e("","CARD_NAME:"+CARD_NAME);
                Log.e("","CARD_NUMBER:"+CARD_NUMBER);


                if (CARD_BACKGROUND_NUMBER == -1) {

                    Toast.makeText(CardAddActivity.this,"카드 배경을 선택하세요!",Toast.LENGTH_LONG).show();

                    Animation shake = AnimationUtils.loadAnimation(CardAddActivity.this, R.anim.shake);
                    beforeLayout.startAnimation(shake);

                    vibe.vibrate(500);
                } else {

                    Card insertCard = new Card(null, CARD_NAME, CARD_NUMBER,"CODE_128" ,CARD_BACKGROUND_NUMBER + "");
                    cardDao.insert(insertCard);
                    vibe.vibrate(30);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {


                            Intent i = getIntent();
                            i.putExtra("CARD", "YES");
                            i.putExtra("CARD_NAME", finalCardTitle);
                            i.putExtra("CARD_BARCODE", finalCardBarcode);
                            setResult(RESULT_OK, i);


                            finish();
                        }
                    }, 30);
                }
            }
        });


        addCardTitle = (EditText) findViewById(R.id.add_card_title_edit_text);
        addCardBarcode = (EditText) findViewById(R.id.add_card_barcode_edit_text);
        addCardBarcodeImage = (ImageView) findViewById(R.id.add_card_barcode_image);
        addCardBarcodeText = (TextView) findViewById(R.id.add_card_barcode_number);


        addCardTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
                finalTitle.setText(s.toString());
                finalCardTitle = s.toString();
                CARD_NAME = s.toString();
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
        });


        addCardBarcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {

                MultiFormatWriter gen = new MultiFormatWriter();
                String data = s.toString();
                finalCardBarcode = s.toString();
                CARD_NUMBER = s.toString();

                addCardBarcodeText.setText(s.toString());


                Log.e("", "data:" + data);
                int WIDTH = 320;
                int HEIGHT = 180;
                try {
                    BitMatrix bytemap = gen.encode(data, BarcodeFormat.CODE_128, WIDTH, HEIGHT);
                    Bitmap bar = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);
                    for (int i = 0; i < WIDTH; ++i)
                        for (int j = 0; j < HEIGHT; ++j) {
                            bar.setPixel(i, j, bytemap.get(i, j) ? Color.parseColor("#000000") : Color.parseColor("#ffffff"));
                        }
                    addCardBarcodeImage.setImageBitmap(bar);
                    addCardBarcodeImage.invalidate();

                    finalBarcode.setImageBitmap(bar);
                    finalBarcode.invalidate();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
        });


        mMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mMetrics);





        addCardTitle.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);


    }

    private GridView.OnItemClickListener gridviewOnItemClickListener
            = new GridView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {

            CARD_BACKGROUND_NUMBER = Integer.parseInt(arg0.getAdapter().getItem(arg2).toString());
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), new Demo().covers[Integer.parseInt(arg0.getAdapter().getItem(arg2).toString())]);
            cardBackgroundSelectImageView.setImageBitmap(bitmap);
            cardBackgroundSelectImageView.invalidate();


        }
    };

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return Demo.covers.length;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {



            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View gridView;

            if (convertView == null) {
                gridView = new View(mContext);
                gridView = inflater.inflate(R.layout.grid_item, null);
            } else {
                gridView = (View) convertView;
            }
            ImageView imageView = (ImageView) gridView
                    .findViewById(R.id.grid_image);
            imageView.setImageResource(Demo.thumbCovers[position]);

            return gridView;

        }
    }

}

