package com.example.filemanagernew;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class MainFragmetn extends Fragment implements FileAdapter.OnFileEvenListener {
    private String pathAddress;
    private FileAdapter fileAdapter;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayout;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=new Bundle();

        pathAddress=getArguments().getString("path");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=LayoutInflater.from(getContext()).inflate(R.layout.main_fragment_rv,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        File file=new File(pathAddress);
        File[] files=file.listFiles();
        fileAdapter=new FileAdapter(Arrays.asList(files),this);

        gridLayout=new GridLayoutManager(getContext(),1,RecyclerView.VERTICAL,false);

        ImageView backImg=view.findViewById(R.id.backIC);
        TextView tvPath=view.findViewById(R.id.txtPath);
        tvPath.setText(pathAddress);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
         recyclerView=view.findViewById(R.id.rvMain);
        recyclerView.setLayoutManager(gridLayout);
        recyclerView.setAdapter(fileAdapter);
    }

    @Override
    public void onOpenFile(File file) {
        if (file.isDirectory()){
            ((MainActivity)getActivity()).onFragmentLoader(new File(pathAddress  + File.separator+file.getName()));
        }
    }

    @Override
    public void onDeleteClicked(File file) {
            fileAdapter.deleteItem(file);
    }

    @Override
    public void onMoveClicked(File file) {
        try {
            copyFiles(file,getDestination(new File(file.getName())));
            onDeleteClicked(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCopyClicked(File file) {
        try {
            copyFiles(file,getDestination(new File(file.getName())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void copyFiles(File resource,File destination) throws IOException {
        FileInputStream inputStream=new FileInputStream(resource);
        FileOutputStream outputStream=new FileOutputStream(destination);
        byte[] buffer=new byte[1024];
        int length;
        while ((length=inputStream.read(buffer))>0){
            outputStream.write(buffer,0,length);
        }
        inputStream.close();
        outputStream.close();
    }
    public File  getDestination(File file){

        return new File(getContext().getExternalFilesDir(null).getPath()+File.separator+"Download"+File.separator+file);
    }

    public void addFolder(String folderName){
        File file=new File(pathAddress+File.separator+folderName);
        if (!file.exists()){
            if (file.mkdir()){
                fileAdapter.addFolder(file);
                recyclerView.smoothScrollToPosition(0);

            }
        }
    }
    public void searched(String query){
        if (fileAdapter!=null){
            fileAdapter.searching(query);
        }
    }
    public void setViewType(ViewForm viewForm) {
        if (fileAdapter != null) {
            fileAdapter.setViewTipe(viewForm);

            if (viewForm == ViewForm.GRID) {
                gridLayout.setSpanCount(2);
            } else {
                gridLayout.setSpanCount(1);
            }
        }
    }
}
