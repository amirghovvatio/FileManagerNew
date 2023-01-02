package com.example.filemanagernew;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButtonToggleGroup;

import java.io.File;

public class MainActivity extends AppCompatActivity implements AddFolderFragment.AddFolderCallBack {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        File file=getExternalFilesDir(null);
        onFragmentLoader(file);
        EditText editText=findViewById(R.id.edtSearch);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.frameMain);
                if (fragment instanceof Fragment){
                    ((MainFragmetn) fragment).searched(editText.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        MaterialButtonToggleGroup toggleGroup=findViewById(R.id.toggleGroupMain);
        toggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (checkedId==R.id.listToggle &&isChecked){
                    Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.frameMain);
                    if (fragment instanceof Fragment){
                        ((MainFragmetn) fragment).setViewType(ViewForm.ROW);
                    }
                    }else if(checkedId==R.id.gridToggle&& isChecked){
                        Fragment fragment1=getSupportFragmentManager().findFragmentById(R.id.frameMain);
                        if (fragment1 instanceof Fragment){
                            ((MainFragmetn) fragment1).setViewType(ViewForm.GRID );
                        }
                    }
                }

        });

        ImageView btnAddFolder=findViewById(R.id.addFolderIC);
        btnAddFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFolderFragment addFolderFragment=new AddFolderFragment();
                addFolderFragment.show(getSupportFragmentManager(),null);
            }
        });

        String str=file.getPath();
    }
    public void onFragmentLoader(File file){
        Fragment fragmentMain=new MainFragmetn();
        Bundle bundle=new Bundle();
        bundle.putString("path",file.getPath());
        fragmentMain.setArguments(bundle);
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameMain,fragmentMain);
        fragmentTransaction.commit();
        fragmentTransaction.addToBackStack(null);
    }

    @Override
    public void onFolderAdded(String add) {
        Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.frameMain);
        if (fragment instanceof Fragment){
            ((MainFragmetn) fragment).addFolder(add);
        }
    }
}