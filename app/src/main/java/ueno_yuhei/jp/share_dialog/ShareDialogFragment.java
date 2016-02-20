package ueno_yuhei.jp.share_dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.util.Pair;
import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Created by ueno-yuhei on 2016/02/20.
 */
public class ShareDialogFragment extends DialogFragment {
    private String shareUrl;
    private String shareMessage;
    private boolean isGooglePlayDl;

    public static final int FACE_BOOK_APP = 0;
    public static final int TWITTER_APP = 1;
    public static final int LINE_APP = 2;
    public static final int CANCEL = 3;

    private final String[] sharePackages = {"com.facebook.katana", "com.twitter.android", "jp.naver.line.android"};
    private ArrayList<Pair<Integer, String>> addList;

    public static ShareDialogFragment newInstance(String shareUrl, String shareMessage, boolean isGooglePlayDl) {
        ShareDialogFragment shareDialogFragment = new ShareDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", shareUrl);
        bundle.putString("message", shareMessage);
        bundle.putBoolean("isGooglePlayDl", isGooglePlayDl);
        shareDialogFragment.setArguments(bundle);
        return shareDialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        shareUrl = getArguments().getString("url");
        shareMessage = getArguments().getString("message");
        isGooglePlayDl = getArguments().getBoolean("isGooglePlayDl", false);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setItems(getItem(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Pair<Integer, String> selectPair = addList.get(which);
                Intent intent = new Intent();
                switch (selectPair.first) {
                    case FACE_BOOK_APP:
                        // Facebook
                        if (isShareAppInstall(FACE_BOOK_APP)) {
                            intent.setAction(Intent.ACTION_SEND);
                            intent.setPackage("com.facebook.katana");
                            intent.setType("text/plain");
                            intent.putExtra(Intent.EXTRA_TEXT, shareUrl);
                            try {
                                startActivity(intent);
                            } catch (Exception e) {
                                //Toast.makeText(getActivity(), "Facebookアプリの起動に失敗しました。\n設定からアプリを無効にしている場合は、Facebookアプリを有効にしてください。", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            if (isGooglePlayDl)
                                shareAppDl(FACE_BOOK_APP);
                        }
                        break;
                    case TWITTER_APP:
                        // Twitter
                        if (isShareAppInstall(TWITTER_APP)) {
                            intent.setAction(Intent.ACTION_SEND);
                            intent.setPackage("com.twitter.android");
                            intent.setType("image/png");
                            intent.putExtra(Intent.EXTRA_TEXT, shareMessage + " " + shareUrl);
                            try {
                                startActivity(intent);
                            } catch (Exception e) {
                                //Toast.makeText(getActivity(), "Twitterアプリの起動に失敗しました。\n設定からアプリを無効にしている場合は、Twitterアプリを有効にしてください。", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            if (isGooglePlayDl)
                                shareAppDl(TWITTER_APP);
                        }
                        break;
                    case LINE_APP:
                        // LINE
                        if (isShareAppInstall(LINE_APP)) {
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse("line://msg/text/" + shareMessage + " " + shareUrl));
                            try {
                                startActivity(intent);
                            } catch (Exception e) {
                                //Toast.makeText(getActivity(), "LINEアプリの起動に失敗しました。\n設定からアプリを無効にしている場合は、LINEアプリを有効にしてください。", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            if (isGooglePlayDl)
                                shareAppDl(LINE_APP);
                        }
                        break;
                    case CANCEL:
                        dismiss();
                        break;

                }
            }
        });
        builder.setCancelable(true);
        return builder.create();
    }

    private CharSequence[] getItem() {
        CharSequence[] charList = new CharSequence[addList.size()];
        for (int i = 0; i < addList.size(); i++) {
            charList[i] = addList.get(i).second;
        }
        return charList;
    }

    public ShareDialogFragment add(int shareAppId) {
        add(shareAppId, "");
        return this;
    }

    public ShareDialogFragment add(int shareAppId, String shareAppName) {
        if (addList == null) {
            addList = new ArrayList<Pair<Integer, String>>();
        }
        if (shareAppId > 3) {
            return this;
        }
        if (TextUtils.isEmpty(shareAppName)) {
            switch (shareAppId) {
                case FACE_BOOK_APP:
                    shareAppName = "Facebook";
                    break;
                case TWITTER_APP:
                    shareAppName = "Twitter";
                    break;
                case LINE_APP:
                    shareAppName = "Line";
                    break;
                case CANCEL:
                    shareAppName = "キャンセル";
                    break;
            }
        }
        addList.add(Pair.create(shareAppId, shareAppName));
        return this;
    }

    // アプリがインストールされているかチェック
    private Boolean isShareAppInstall(int shareId) {
        try {
            PackageManager pm = getActivity().getPackageManager();
            pm.getApplicationInfo(sharePackages[shareId], PackageManager.GET_META_DATA);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    // アプリが無かったのでGooglePlayに飛ばす
    private void shareAppDl(int shareId) {
        Uri uri = Uri.parse("market://details?id=" + sharePackages[shareId]);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
