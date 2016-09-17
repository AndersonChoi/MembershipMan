package man.membership.com.membershipman;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
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

import com.crashlytics.android.Crashlytics;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.hanks.htextview.HTextView;
import com.hanks.htextview.HTextViewType;
import com.membership.card.dao.Card;
import com.membership.card.dao.CardDao;
import com.membership.card.dao.DaoMaster;
import com.membership.card.dao.DaoSession;
import com.vstechlab.easyfonts.EasyFonts;

import org.w3c.dom.Text;

import de.greenrobot.dao.DaoLog;
import io.fabric.sdk.android.Fabric;
import me.crosswall.lib.coverflow.CoverFlow;

public class MainActivity extends Activity {


    private int CARD_BACKGROUND_NUMBER = -1;
    private String CARD_NAME = "";
    private String CARD_NUMBER = "";
    private String CARD_TYPE = "";

    private static boolean doubleBackToExitPressedOnce = false;

    private static boolean fullCardViewOn=false;


    private final int PICK_ADD_CARD = 1;
    private final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;


    private String LONG_CLICK_DELETE = "아래 카드를 길게 누르시면 카드 삭제가 가능합니다";


    private ViewPager viewPager;
    private RelativeLayout mainCardLayout;
    private ImageView mainImageView;
    private Vibrator vibe;
    private TextView mainCardTitle;
    private ImageView mainCardBarcode;
    private FloatingActionButton buttonSetting, buttonAddCard, buttonEditCard, buttonShare;
    private FloatingActionsMenu menuMultipleActions;

    private ImageView fullLayout, fullLayoutForAnimation;

    private float cardTitleY;
    private float cardBarcodeY;

    private int mainCardPosition = 0;
    private int pagerCardPosition = 0;

    private Button miniLight;

    private TextView logoInside;

    private RelativeLayout fullBarcodeLayout;
    private Button fullBarcodeButton;
    private ImageView fullBarcodeImageView;


    private DaoMaster.DevOpenHelper helper = null;
    private SQLiteDatabase db = null;
    private DaoMaster daoMaster = null;
    private DaoSession daoSession = null;
    private CardDao cardDao = null;
    private Cursor cursor = null;


    private RelativeLayout firstTimeLayout;

    private ImageView firstLogo;

    private HTextView logoText;
    private HTextView logoText2;
    private HTextView logoText3;

    private Button startButton;
    private Button startContinueButton;
    private RelativeLayout firstCardAddLayout;

    private EditText firstCardEditTitle, firstCardEditBarcode;

    private RelativeLayout lastCardLayout;

    private TextView firstTimeCardTitle;
    private ImageView firstTimeCardBarcode;

    private DisplayMetrics mMetrics;
    private GridView gridview;


    private ImageView cardBackgroundSelectImageView;

    private Button finalAddCardButton;

    private ImageView arrowImage;


    private TextView fullBarcodeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);


        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(MainActivity.this, "card.db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        cardDao = daoSession.getCardDao();


        /*
        Card insertCard = new Card(null,"L.POINT","8710400841435064","20");
        cardDao.insert(insertCard);
        insertCard = new Card(null,"교보북클럽","2000423228200001","18");
        cardDao.insert(insertCard);
        insertCard = new Card(null,"CJ ONE","7761010393536924","2");
        cardDao.insert(insertCard);
        insertCard = new Card(null,"신세계 포인트","9350169011684302","1");
        cardDao.insert(insertCard);
        insertCard = new Card(null,"올레 맴버쉽","2917101095231653","4");
        cardDao.insert(insertCard);*/

        cursor = db.query(cardDao.getTablename(), cardDao.getAllColumns(), null, null, null, null, null);
        DaoLog.d(DatabaseUtils.dumpCursorToString(cursor));


        // Card deleteCard = cardDao.loadAll().get(3);
        //cardDao.delete(deleteCard);
        // Card insertCard = new Card(null,"올레 맴버쉽","2917101095231653","4");
        //cardDao.insert(insertCard);


        for (int i = 0; i < cardDao.loadAll().size(); i++) {
            DaoLog.d(cardDao.loadAll().get(i).getCardName());
        }

        gridview = (GridView) findViewById(R.id.gridview2);


        fullBarcodeText=(TextView)findViewById(R.id.main_card_barcode_text);
        finalAddCardButton = (Button) findViewById(R.id.final_add_first_card_button);
        arrowImage = (ImageView) findViewById(R.id.arrow_image);

        cardBackgroundSelectImageView = (ImageView) findViewById(R.id.last_card_add_background);
        firstTimeCardTitle = (TextView) findViewById(R.id.main_card_title_first_time);
        firstTimeCardBarcode = (ImageView) findViewById(R.id.main_card_barcode_first_time);

        lastCardLayout = (RelativeLayout) findViewById(R.id.last_card_add_layout);

        firstCardEditTitle = (EditText) findViewById(R.id.add_first_card_edit_title);
        firstCardEditBarcode = (EditText) findViewById(R.id.add_first_card_edit_number);
        startContinueButton = (Button) findViewById(R.id.start_add_card_button);
        firstCardAddLayout = (RelativeLayout) findViewById(R.id.first_card_add_layout);
        startButton = (Button) findViewById(R.id.start_button);
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(22);
        firstLogo = (ImageView) findViewById(R.id.start_logo);
        mainCardTitle = (TextView) findViewById(R.id.main_card_title);
        mainCardBarcode = (ImageView) findViewById(R.id.main_card_barcode);
        logoInside = (TextView) findViewById(R.id.logo_inside);
        mainCardLayout = (RelativeLayout) findViewById(R.id.main_card_layout);
        mainImageView = (ImageView) findViewById(R.id.main_card_background);
        menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        buttonSetting = (FloatingActionButton) findViewById(R.id.action_settings);
        buttonAddCard = (FloatingActionButton) findViewById(R.id.action_add_card);
        buttonEditCard = (FloatingActionButton) findViewById(R.id.action_edit_card);
        buttonShare = (FloatingActionButton) findViewById(R.id.action_share_card);
        fullBarcodeLayout = (RelativeLayout) findViewById(R.id.full_barcode_layout);
        fullBarcodeButton = (Button) findViewById(R.id.full_barcode_button);
        fullBarcodeImageView = (ImageView) findViewById(R.id.full_barcode_image_view);


        logoText = (HTextView) findViewById(R.id.logo_text);
        logoText2 = (HTextView) findViewById(R.id.logo_text2);
        logoText3 = (HTextView) findViewById(R.id.logo_text3);

        fullLayout = (ImageView) findViewById(R.id.full_layout);
        fullLayoutForAnimation = (ImageView) findViewById(R.id.full_layout2);
        miniLight = (Button) findViewById(R.id.mini_light);

        logoInside.setTypeface(EasyFonts.cac_champagne(this));


        buttonSetting.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                menuMultipleActions.collapse();
                vibe.vibrate(22);

                Intent i = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(i);
            }
        });
        buttonAddCard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                menuMultipleActions.collapse();
                vibe.vibrate(22);
                Intent i = new Intent(MainActivity.this, CardAddActivity.class);
                startActivityForResult(i, PICK_ADD_CARD);

            }
        });
        buttonEditCard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                menuMultipleActions.collapse();
                vibe.vibrate(22);
            }
        });

        buttonShare.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                menuMultipleActions.collapse();

                vibe.vibrate(22);

                checkPermission();

                mainCardLayout.setDrawingCacheEnabled(true);
                final Bitmap s = mainCardLayout.getDrawingCache();

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;
                Bitmap resize = Bitmap.createScaledBitmap(s, s.getWidth(), s.getHeight(), true);

                Matrix m = new Matrix();
                m.setRotate(0, resize.getWidth(), resize.getHeight());

                Bitmap rotateBitmap = Bitmap.createBitmap(
                        resize, 0, 0, resize.getWidth(), resize.getHeight(), m, false);


                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.custom_dialog);
                dialog.setTitle("Share barcode");

                ImageView iv = (ImageView) dialog.findViewById(R.id.image);
                iv.setImageBitmap(rotateBitmap);
                iv.invalidate();
                Button shareButton = (Button) dialog.findViewById(R.id.share_button);

                mainCardLayout.setDrawingCacheEnabled(false);
                shareButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {


                        mainCardLayout.setDrawingCacheEnabled(true);
                        Bitmap s2 = mainCardLayout.getDrawingCache();
                        String pathofBmp = MediaStore.Images.Media.insertImage(getContentResolver(), s2, "title", null);
                        Uri bmpUri = Uri.parse(pathofBmp);
                        Intent emailIntent1 = new Intent(android.content.Intent.ACTION_SEND);
                        emailIntent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        emailIntent1.putExtra(Intent.EXTRA_STREAM, bmpUri);
                        emailIntent1.setType("image/png");
                        startActivity(emailIntent1);
                        vibe.vibrate(22);
                        mainCardLayout.setDrawingCacheEnabled(false);
                        dialog.dismiss();

                    }
                });

                dialog.show();


            }

        });

        miniLight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                menuMultipleActions.collapse();
                vibe.vibrate(22);


                fullCardViewOn=true;
                fullBarcodeLayout.animate()
                        .y(0)
                        .alpha(0f)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(0)
                        .start();

                fullBarcodeLayout.setVisibility(View.VISIBLE);
                fullBarcodeLayout.animate()
                        .y(0)
                        .alpha(1f)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setInterpolator(AnimationUtils.loadInterpolator(MainActivity.this, android.R.anim.decelerate_interpolator))
                        .setDuration(300)
                        .start();

            }
        });
        fullBarcodeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fullCardViewOn=false;
                menuMultipleActions.collapse();
                vibe.vibrate(22);
                fullBarcodeLayout.setVisibility(View.GONE);
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                firstLogo.animate()
                        .y(-300)
                        .scaleX(0.4f)
                        .scaleY(0.4f)
                        .setInterpolator(AnimationUtils.loadInterpolator(MainActivity.this, android.R.anim.decelerate_interpolator))
                        .setDuration(1000)
                        .start();


                firstCardAddLayout.setVisibility(View.VISIBLE);
                firstCardAddLayout.animate()
                        .alpha(0f)
                        .setDuration(0)
                        .start();

                firstCardAddLayout.animate()
                        .alpha(1f)
                        .setInterpolator(AnimationUtils.loadInterpolator(MainActivity.this, android.R.anim.decelerate_interpolator))
                        .setDuration(1000)
                        .start();

                startButton.setVisibility(View.GONE);
                vibe.vibrate(22);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        logoText2.setTypeface(Typeface.createFromAsset(getAssets(), "NotoSansKR-Light-Hestia.otf"));
                        logoText2.setAnimateType(HTextViewType.LINE);
                        logoText2.animateText("Continue");


                        firstCardEditTitle.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                    }
                }, 700);


            }
        });


        startContinueButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (firstCardEditTitle.getText().length() == 0 || firstCardEditBarcode.getText().length() == 0) {
                    Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake);
                    firstCardAddLayout.startAnimation(shake);
                    vibe.vibrate(400);
                } else {
                    vibe.vibrate(22);
                    CARD_NAME = firstCardEditTitle.getText().toString();
                    CARD_NUMBER = firstCardEditBarcode.getText().toString();

                    firstTimeCardTitle.setText(CARD_NAME);


                    MultiFormatWriter gen = new MultiFormatWriter();
                    String data = CARD_NUMBER;
                    int WIDTH = 320;
                    int HEIGHT = 180;
                    try {
                        BitMatrix bytemap = gen.encode(data, BarcodeFormat.CODE_128, WIDTH, HEIGHT);
                        Bitmap bar = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);
                        for (int i = 0; i < WIDTH; ++i)
                            for (int j = 0; j < HEIGHT; ++j) {
                                bar.setPixel(i, j, bytemap.get(i, j) ? Color.parseColor("#000000") : Color.parseColor("#00ffffff"));
                            }
                        firstTimeCardBarcode.setImageBitmap(bar);
                        firstTimeCardBarcode.invalidate();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    lastCardLayout.setVisibility(View.VISIBLE);
                    lastCardLayout.animate()
                            .alpha(0f)
                            .setDuration(0)
                            .start();

                    lastCardLayout.animate()
                            .alpha(1f)
                            .setInterpolator(AnimationUtils.loadInterpolator(MainActivity.this, android.R.anim.decelerate_interpolator))
                            .setDuration(1000)
                            .start();


                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            logoText3.setTypeface(Typeface.createFromAsset(getAssets(), "NotoSansKR-Light-Hestia.otf"));
                            logoText3.setAnimateType(HTextViewType.LINE);
                            logoText3.animateText("Add card");

                        }
                    }, 700);

                }

            }
        });


        finalAddCardButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                if (CARD_BACKGROUND_NUMBER == -1) {

                    Toast.makeText(MainActivity.this, "카드 배경을 선택하세요!", Toast.LENGTH_LONG).show();

                    Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake);
                    lastCardLayout.startAnimation(shake);

                    vibe.vibrate(500);
                } else {

                    Card insertCard = new Card(null, CARD_NAME, CARD_NUMBER, "CODE_128", CARD_BACKGROUND_NUMBER + "");
                    cardDao.insert(insertCard);
                    vibe.vibrate(30);


                    SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("checkFirstTime", false);
                    editor.commit();

                    Boolean isInsertDeleteCard = pref.getBoolean("checkDeleteCard", false);
                    if (!isInsertDeleteCard) {
                        Card insertCard2 = new Card((long) 100, "길게눌러 삭제", "0000000000", "CODE_128", "20");
                        cardDao.insert(insertCard2);
                        SharedPreferences.Editor editor2 = pref.edit();
                        editor2.putBoolean("checkDeleteCard", true);
                        editor2.commit();
                    }


                    recreate();


                }


            }
        });


        firstTimeLayout = (RelativeLayout) findViewById(R.id.firstTimeLayout);

        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        Boolean isStartFirstTime = pref.getBoolean("checkFirstTime", true);

        if (isStartFirstTime) {

            gridview.setAdapter(new ImageAdapter2(this));
            gridview.setOnItemClickListener(gridviewOnItemClickListener2);


            firstTimeLayout.setVisibility(View.VISIBLE);

            float logoXY = firstLogo.getTop();

            firstLogo.animate()
                    .y(logoXY + 60)
                    .alpha(0f)
                    .setDuration(0)
                    .start();

            firstLogo.animate()
                    .y(logoXY)
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setInterpolator(AnimationUtils.loadInterpolator(MainActivity.this, android.R.anim.decelerate_interpolator))
                    .setDuration(1400)
                    .start();


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    logoText.setTypeface(Typeface.createFromAsset(getAssets(), "NotoSansKR-Light-Hestia.otf"));
                    logoText.setAnimateType(HTextViewType.LINE);
                    logoText.animateText("Welecome to Membership man");


                    arrowImage.setVisibility(View.VISIBLE);
                    Animation mAnimation;
                    mAnimation = new TranslateAnimation(
                            TranslateAnimation.ABSOLUTE, 0f,
                            TranslateAnimation.ABSOLUTE, 0f,
                            TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                            TranslateAnimation.RELATIVE_TO_PARENT, 0.01f);
                    mAnimation.setDuration(300);
                    mAnimation.setRepeatCount(-1);
                    mAnimation.setRepeatMode(Animation.REVERSE);
                    mAnimation.setInterpolator(new LinearInterpolator());
                    arrowImage.setAnimation(mAnimation);

                }
            }, 700);








/*


            {
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("checkFirstTime", false);
                editor.commit();
                recreate();
            }


*/
        } else {
            firstTimeLayout.setVisibility(View.GONE);

            cardAnimation(0, false);
        }


        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new MyPagerAdapter());
        viewPager.setClipChildren(false);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                menuMultipleActions.collapse();
                pagerCardPosition = position;
            }

            @Override
            public void onPageScrolled(int position, float positionOffest, int positionOffsetPixels) {
                menuMultipleActions.collapse();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                menuMultipleActions.collapse();
            }
        });


        new CoverFlow.Builder()
                .with(viewPager)
                .scale(0f)
                .pagerMargin(10f)
                .spaceSize(10f)
                .build();


        mMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }


    private void cardAnimation(final int cover, final boolean childAnimation) {


        if (childAnimation) {
            cardTitleY = mainCardTitle.getTop();
            cardBarcodeY = mainCardBarcode.getTop();
            mainCardTitle.animate()
                    .y(cardTitleY + 60)
                    .alpha(0f)
                    .setDuration(0)
                    .start();
            mainCardBarcode.animate()
                    .y(cardBarcodeY + 60)
                    .alpha(0f)
                    .setDuration(0)
                    .start();
        }


        mainCardLayout.animate()
                .setDuration(200)
                .scaleX(1f)
                .scaleY(1f)
                .alpha(0f)
                .start();


        Thread readyThread = new Thread(new Runnable() {
            @Override
            public void run() {

                final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), new Demo().covers[Integer.parseInt(cardDao.loadAll().get(cover).getCardBackground())]);

                MultiFormatWriter gen = new MultiFormatWriter();
                MultiFormatWriter gen2 = new MultiFormatWriter();
                final String data = cardDao.loadAll().get(cover).getCardNumber();
                int WIDTH = 400;
                int HEIGHT = 100;
                try {
                    BitMatrix bytemap = gen.encode(data, BarcodeFormat.CODE_128, WIDTH, HEIGHT);
                    final Bitmap bar = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);
                    for (int i = 0; i < WIDTH; ++i)
                        for (int j = 0; j < HEIGHT; ++j) {
                            bar.setPixel(i, j, bytemap.get(i, j) ? Color.parseColor("#ff000000") : Color.parseColor("#00ffffff"));
                        }

                    BitMatrix bytemap2 = gen2.encode(data, BarcodeFormat.CODE_128, WIDTH, HEIGHT);
                    final Bitmap bar2 = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);

                    for (int i = 0; i < WIDTH; ++i)
                        for (int j = 0; j < HEIGHT; ++j) {
                            bar2.setPixel(i, j, bytemap2.get(i, j) ? Color.parseColor("#000000") : Color.parseColor("#ffffff"));
                        }


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            fullBarcodeText.setText(data);
                            mainImageView.setImageBitmap(bitmap);

                            mainCardBarcode.setImageBitmap(bar);
                            mainCardBarcode.invalidate();
                            fullBarcodeImageView.setImageBitmap(bar2);
                            fullBarcodeImageView.invalidate();


                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        readyThread.start();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mainCardLayout.animate()
                        .y(2000)
                        .setDuration(0)
                        .scaleX(1f)
                        .scaleY(1f)
                        .alpha(0f)
                        .start();


                mainCardTitle.setText(cardDao.loadAll().get(cover).getCardName());

                mainCardLayout.animate()
                        .y(0)
                        .alpha(1f)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setInterpolator(AnimationUtils.loadInterpolator(MainActivity.this, android.R.anim.decelerate_interpolator))
                        .setDuration(200)
                        .start();

                if (childAnimation) {

                    mainCardTitle.animate()
                            .y(cardTitleY)
                            .alpha(1f)
                            .scaleX(1f)
                            .scaleY(1f)
                            .setInterpolator(AnimationUtils.loadInterpolator(MainActivity.this, android.R.anim.decelerate_interpolator))
                            .setDuration(1400)
                            .start();
                    mainCardBarcode.animate()
                            .y(cardBarcodeY)
                            .alpha(1f)
                            .scaleX(1f)
                            .scaleY(1f)
                            .setInterpolator(AnimationUtils.loadInterpolator(MainActivity.this, android.R.anim.decelerate_interpolator))
                            .setDuration(1400)
                            .start();
                }

            }
        }, 200);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), new Demo().covers[Integer.parseInt(cardDao.loadAll().get(cover).getCardBackground())]);

                RenderScript rs = RenderScript.create(getApplicationContext());
                Allocation input = Allocation.createFromBitmap(rs, bitmap);
                Allocation output = Allocation.createTyped(rs, input.getType());
                ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
                script.setRadius(25.f);
                script.setInput(input);
                script.forEach(output);
                output.copyTo(bitmap);


                fullLayoutForAnimation.setVisibility(View.VISIBLE);
                fullLayoutForAnimation.setImageBitmap(bitmap);

                Animation ani = new AlphaAnimation(0.0f, 1.0f);
                ani.setDuration(1200);
                ani.setFillAfter(true);
                ani.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        fullLayout.setImageBitmap(bitmap);
                        fullLayoutForAnimation.setVisibility(View.GONE);
                    }
                });

                fullLayoutForAnimation.setAnimation(ani);
                ani.start();


            }
        }, 200);


    }


    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {


            final View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.card_layout, null);
            final TextView tv = (TextView) view.findViewById(R.id.back_count);
            final ImageView cardBackground = (ImageView) view.findViewById(R.id.card_background);
            final Button image = (Button) view.findViewById(R.id.card_button);
            final int cPos = position;
            cardBackground.setImageDrawable(getResources().getDrawable(new Demo().thumbCovers[Integer.parseInt(cardDao.loadAll().get(cPos).getCardBackground())]));
            tv.setText(cardDao.loadAll().get(cPos).getCardName());
            image.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (mainCardPosition != pagerCardPosition) {
                        cardAnimation(cPos, true);
                        vibe.vibrate(22);
                        mainCardPosition = cPos;


                        if (cardDao.loadAll().get(cPos).getCardName().equals("길게눌러 삭제") && Integer.parseInt(cardDao.loadAll().get(cPos).getCardBackground()) == 20) {
                            Toast.makeText(MainActivity.this, LONG_CLICK_DELETE, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake);
                        viewPager.startAnimation(shake);
                        vibe.vibrate(100);
                    }

                }
            });
            image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(MainActivity.this);
                    alert_confirm.setTitle("Delete card").setMessage("정말로 카드를 삭제하시겠습니까?").setCancelable(true).setPositiveButton("Delete",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    if (cardDao.loadAll().size() == 1) {

                                        Toast.makeText(MainActivity.this, "더 이상 삭제할 수 없습니다!", Toast.LENGTH_LONG).show();
                                        vibe.vibrate(500);

                                    } else {
                                        Card deleteCard = cardDao.loadAll().get(cPos);
                                        Toast.makeText(MainActivity.this, deleteCard.getCardName() + " 카드를 삭제하였습니다!", Toast.LENGTH_LONG).show();
                                        cardDao.delete(deleteCard);
                                        recreate();
                                    }


                                }
                            });
                    AlertDialog alert = alert_confirm.create();
                    alert.show();


                    return true;
                }
            });

            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return cardDao.loadAll().size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }

    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        Log.e("ssssss", " requestCode " + requestCode);
        if (requestCode == PICK_ADD_CARD) {

            Log.e("ssssss", " resultCode " + resultCode);

            if (resultCode == RESULT_OK) {


                Log.e("ssssss", " data.getStringExtra(CARD) :" + data.getStringExtra("CARD"));


                if (data.getStringExtra("CARD").equals("YES")) {

                    Log.e("ssssss", " CARD_NAME " + data.getStringExtra("CARD_NAME"));
                    Log.e("ssssss", " CARD_BARCODE " + data.getStringExtra("CARD_BARCODE"));


                    //viewPager.setCurrentItem(0);
                    recreate();


                }

            }
        }
    }


    private GridView.OnItemClickListener gridviewOnItemClickListener2
            = new GridView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> parent, View v,
                                int position, long id) {


            CARD_BACKGROUND_NUMBER = Integer.parseInt(parent.getAdapter().getItem(position).toString());
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), new Demo().covers[Integer.parseInt(parent.getAdapter().getItem(position).toString())]);
            cardBackgroundSelectImageView.setImageBitmap(bitmap);
            cardBackgroundSelectImageView.invalidate();

        }
    };

    public class ImageAdapter2 extends BaseAdapter {
        private Context mContext;

        public ImageAdapter2(Context c) {
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


    @Override
    public void onBackPressed() {

        menuMultipleActions.collapse();


        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        if(fullCardViewOn)
        {
            fullCardViewOn=false;
            menuMultipleActions.collapse();
            vibe.vibrate(22);
            fullBarcodeLayout.setVisibility(View.GONE);

        }else{


            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "한번더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 1400);

        }



    }

    @Override
    public void onResume() {
        super.onResume();


        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        Boolean isStartFirstTime = pref.getBoolean("checkFirstTime", true);

        if (!isStartFirstTime) {
            //viewPager.setCurrentItem(0);
            //cardAnimation(0, false);
            pagerCardPosition = 0;
        }

    }
}
