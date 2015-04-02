package awsomethree.com.townkitchen.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;

import awsomethree.com.townkitchen.R;
import awsomethree.com.townkitchen.interfaces.ParseQueryCallback;
import awsomethree.com.townkitchen.interfaces.dialogInterfaceListener;
import awsomethree.com.townkitchen.models.Feedback;

/**
 * Created by Long on 4/2/15.
 */
public class GiveFeedbackDialog extends DialogFragment implements ParseQueryCallback {
    private dialogInterfaceListener mListener;
    private Feedback mfeedback;

    protected EditText etComment;
    protected RatingBar ratingBar;

    public static GiveFeedbackDialog newInstance(Fragment targetFragment, Feedback feedback){
        GiveFeedbackDialog newDialog = new GiveFeedbackDialog();
        newDialog.setDialogInterfaceListener((dialogInterfaceListener) targetFragment);
        newDialog.setShoppingCartModel(feedback);
        return newDialog;
    }

    public void setDialogInterfaceListener(dialogInterfaceListener listener){
        this.mListener = listener;
    }

    public void setShoppingCartModel(Feedback feedback){
        this.mfeedback = feedback;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View bodyView = inflater.inflate(R.layout.dialog_rating, null);

        etComment = (EditText) bodyView.findViewById(R.id.etComment);
        ratingBar = (RatingBar) bodyView.findViewById(R.id.ratingBar);

        builder
//                uncomment this for custom title
//                .setCustomTitle(titleView)
                .setView(bodyView)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Look at onStart method which will override this
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mListener != null) {
                            mListener.onFailDialog();
                        }
                        getDialog().cancel();
                    }
                })
        ;

        return builder.create();
    }

    @Override
    public void onStart() {//on OK
        super.onStart();

        AlertDialog d = (AlertDialog) getDialog();
        if (d != null){
            Button positiveButton =  (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean allValid = true;
                    if (etComment.getText().toString().isEmpty()) {
                        etComment.setError("Feedback comment required.");
                        allValid = false;
                    }

                    if (mListener != null && allValid) {
                        //mListener.onSuccessDialog();
                        //getDialog().dismiss();
                        saveFeedback();
                    }
                }
            });
        }
    }


    public void saveFeedback(){
        Feedback.saveFeedback(this, Feedback.FEED_CODE, mfeedback, getDialog().getContext());
    }


    @Override
    public void parseQueryDone(List<? extends ParseObject> parseObjects, ParseException e,
            int queryCode) {
        if (queryCode == Feedback.FEED_CODE){
            //ShoppingCart.clearShoppingCart(getActivity().getApplicationContext());
            mListener.onSuccessDialog();
            getDialog().dismiss();
        }
    }
}
