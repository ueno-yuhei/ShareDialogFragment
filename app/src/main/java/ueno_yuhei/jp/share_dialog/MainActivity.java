package ueno_yuhei.jp.share_dialog;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ShareButton Tap
        findViewById(R.id.share_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ShareDialog Show!
                ShareDialogFragment shareDialogFragment = ShareDialogFragment.newInstance("http://www.yahoo.co.jp/", "yahooのページだよ", false, false);
                shareDialogFragment
                        .add(ShareDialogFragment.GOOGLE_PLUS_APP)
                        .add(ShareDialogFragment.FACE_BOOK_APP)
                        .add(ShareDialogFragment.TWITTER_APP)
                        .add(ShareDialogFragment.LINE_APP)
                        .add(ShareDialogFragment.CANCEL);
                shareDialogFragment.show(getSupportFragmentManager(), "ShareDialogFragment");
            }
        });
    }
}
