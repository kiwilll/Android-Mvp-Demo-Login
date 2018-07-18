package com.hw.mvpbase.baseview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.hw.mvpbase.baseview.mvp.BaseView;


/**
 * Created by hw on 5/17/17.<br>
 */

public abstract class BaseFragment extends Fragment implements BaseView {

//    private Dialog mLoadingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dismissLoadingDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    protected String getPageName() {
        return "";
    }

//    @Override
//    public void showLoadingDialog(CharSequence title, CharSequence msg) {
//        showLoadingDialog(title, msg, false);
//    }

//    @Override
//    public void showLoadingDialog(CharSequence title, CharSequence msg, boolean cancelable) {
//        if (getActivity() == null)
//            return;
//        dismissLoadingDialog();
////        mLoadingDialog = new BwtLoadingDialog(getActivity()).setLoadingMessage(msg);
////        mLoadingDialog.setCancelable(cancelable);
////        mLoadingDialog.show();
//    }

//    @Override
//    public void dismissLoadingDialog() {
//        if (mLoadingDialog != null) {
//            mLoadingDialog.dismiss();
//            mLoadingDialog = null;
//        }
//    }

    @Override
    public void toastMessage(String msg) {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        }
    }

//    @Override
//    public void showAlertDialog(CharSequence title, CharSequence msg, CharSequence[] buttons, final OnAlertDialogCallback callback) {
//        if (getActivity() == null)
//            return;
//        BwtAlertDialog.Builder builder = new BwtAlertDialog.Builder(getActivity())
//                .setTitle(title)
//                .setMessage(msg)
//                .setButtons(buttons, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (callback != null) {
//                            callback.onClick(getActivity(), which);
//                        }
//                    }
//                });
//        builder.create().show();
//    }

    @Override
    public void closeCurrPage() {
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    @Override
    public void hideSoftKeyboard() {
        if (getActivity() != null) {
//            KeyBoardUtil.hideKeyboard(getActivity());
        }
    }

    @Override
    public void showSoftKeyboard() {
        if (getActivity() != null) {
//            KeyBoardUtil.openKeyboard(getActivity());
        }
    }
}
