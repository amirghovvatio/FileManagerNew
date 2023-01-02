package com.example.filemanagernew;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class AddFolderFragment extends DialogFragment {
    private TextInputEditText folderName;
    private MaterialButton btnSave;
    private AddFolderCallBack callBack;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callBack= (AddFolderCallBack) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        View view= LayoutInflater.from(getContext()).inflate(R.layout.add_folder_dialog,null,false);
        builder.setView(view);
        folderName=view.findViewById(R.id.edtEDT);
        btnSave=view.findViewById(R.id.createFolderBTN);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                callBack.onFolderAdded(folderName.getText().toString());
                dismiss();
            }
        });



        return builder.create();
    }
    public interface AddFolderCallBack{
        void onFolderAdded(String add);
    }
}
