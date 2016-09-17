package man.membership.com.membershipman;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.hanks.htextview.HTextView;
import com.hanks.htextview.HTextViewType;

/**
 * Created by HackerAnderson on 16. 9. 15..
 */
public class SettingActivity extends Activity {


    private HTextView setText1, setText2, setText3,setText4;

    private ImageView settingLogo;


    private Vibrator vibe;

    private float logoY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        settingLogo=(ImageView)findViewById(R.id.setting_logo);
        setText1 = (HTextView) findViewById(R.id.htext_logo1);
        setText2 = (HTextView) findViewById(R.id.htext_logo2);
        setText3 = (HTextView) findViewById(R.id.htext_logo3);
        setText4= (HTextView) findViewById(R.id.htext_logo4);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setText1.setTypeface(Typeface.createFromAsset(getAssets(), "NotoSansKR-Light-Hestia.otf"));
                setText1.setAnimateType(HTextViewType.SCALE);
                setText1.animateText("Membership Man");



                logoY = settingLogo.getTop();
                settingLogo.setVisibility(View.VISIBLE);
                settingLogo.animate()
                       // .y(logoY + 60)
                        .alpha(0f)
                        .setDuration(0)
                        .start();

                settingLogo.animate()
                       //rr-= .y(logoY)
                        .alpha(1f)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setInterpolator(AnimationUtils.loadInterpolator(SettingActivity.this, android.R.anim.decelerate_interpolator))
                        .setDuration(1400)
                        .start();

            }
        }, 200);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setText2.setTypeface(Typeface.createFromAsset(getAssets(), "NotoSansKR-Light-Hestia.otf"));
                setText2.setAnimateType(HTextViewType.LINE);
                setText2.animateText("Send feedback");
            }
        }, 400);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setText3.setTypeface(Typeface.createFromAsset(getAssets(), "NotoSansKR-Light-Hestia.otf"));
                setText3.setAnimateType(HTextViewType.LINE);
                setText3.animateText("About");
            }
        }, 800);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setText4.setTypeface(Typeface.createFromAsset(getAssets(), "NotoSansKR-Light-Hestia.otf"));
                setText4.setAnimateType(HTextViewType.LINE);
                setText4.animateText("Rated feed");
            }
        }, 600);




        setText2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String version="none";
                try {
                    PackageInfo i = getPackageManager().getPackageInfo(getPackageName(), 0);
                    version = i.versionName;
                } catch(PackageManager.NameNotFoundException e) { }


                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts( "mailto","coala.movement@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Membership man feedback");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi!\nApp name : MemberShip man\nApp ver : "+version+"\n");
                startActivity(Intent.createChooser(emailIntent, "Choose Email Client"));
                vibe.vibrate(22);
            }
        });




        setText3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://smart-card.mybluemix.net/"));
                startActivity(intent);
                vibe.vibrate(22);

            }
        });


        setText4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=" + getPackageName()));
                startActivity(intent);
                vibe.vibrate(22);

            }
        });









    }


}
