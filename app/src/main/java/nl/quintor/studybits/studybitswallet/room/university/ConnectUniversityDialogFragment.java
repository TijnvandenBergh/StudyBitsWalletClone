package nl.quintor.studybits.studybitswallet.room.university;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import nl.quintor.studybits.studybitswallet.R;

public class ConnectUniversityDialogFragment extends DialogFragment {
    ConnectDialogListener connectDialogListener;
    private EditText endpointEditText;
    private EditText usernameEditText;

    public ConnectUniversityDialogFragment() {
    }

    public void setConnectDialogListener(ConnectDialogListener connectDialogListener) {
        this.connectDialogListener = connectDialogListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        View view = inflater.inflate(R.layout.dialog_connect_university, container, false);
        endpointEditText = view.findViewById(R.id.university_endpoint_text);
        usernameEditText = view.findViewById(R.id.student_id_text);
        return view;
    }


    public EditText getUsername() {
        return usernameEditText;
    }

    public EditText getEndpoint() {
        return endpointEditText;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_connect_university, null))
                // Add action buttons
                .setPositiveButton(R.string.connect, (dialog, id) -> {
                    // sign in the user ...
                    connectDialogListener.onConnectDialogClick();
                    dismiss();
                });
        return builder.create();
    }

    static interface ConnectDialogListener {
        public void onConnectDialogClick();
    }
}