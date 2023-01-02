package com.example.filemanagernew;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.MyViewHolding> {
    private List<File> fileList=new ArrayList<>();
    private OnFileEvenListener onFileEvenListener;
    private List<File> filteredFiles;
    private ViewForm viewTipe=ViewForm.ROW;
    public FileAdapter(List<File> fileList,OnFileEvenListener onFileEvenListener){
        this.fileList = new ArrayList<>(fileList);
        this.filteredFiles = this.fileList;
       this.onFileEvenListener=onFileEvenListener;
    }
    @NonNull
    @Override
    public MyViewHolding onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(viewType== ViewForm.ROW.getValue() ? R.layout.list_rv:R.layout.grid_rv,parent,false);
        return new MyViewHolding(view);
    }

    public void setViewTipe(ViewForm viewTipe) {
        this.viewTipe = viewTipe;
        notifyDataSetChanged();
    }
    @Override
    public int getItemViewType(int position) {

        return viewTipe.getValue();
    }
    public void deleteItem(File file){
        int index=fileList.indexOf(file);
        if (index>-1){
            fileList.remove(index);
            notifyItemRemoved(index);
        }
    }
    public void addFolder(File file){
        this.fileList.add(0,file);
        notifyItemInserted(0);
    }

    public void searching(String query) {
        if (query.length() > 0) {
            List<File> result = new ArrayList<>();
            for (File file :
                    this.fileList) {
                if (file.getName().toLowerCase().contains(query.toLowerCase())) {
                    result.add(file);
                }
            }

            this.filteredFiles = result;
            notifyDataSetChanged();
        } else {
            this.filteredFiles = this.fileList;
            notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolding holder, int position) {
        holder.bindFile(filteredFiles.get(position));
    }

    @Override
    public int getItemCount() {
        return filteredFiles.size();
    }

    public class MyViewHolding extends RecyclerView.ViewHolder {
        private TextView tvFolderName;
        private ImageView imgFolder;
        private ImageView moreIc;
        public MyViewHolding(@NonNull View itemView) {
            super(itemView);
            tvFolderName=itemView.findViewById(R.id.tvFolderFileName);
            imgFolder=itemView.findViewById(R.id.imgFolderIC);
            moreIc=itemView.findViewById(R.id.moreIC);
        }
        public void bindFile(File file){
            moreIc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu=new PopupMenu(view.getContext(),view);
                    popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()){
                                case R.id.copyPop:
                                        onFileEvenListener.onCopyClicked(file);
                                    break;
                                case R.id.movePop:
                                    onFileEvenListener.onMoveClicked(file);
                                    break;
                                case R.id.deletePop:
                                    onFileEvenListener.onDeleteClicked(file);
                                    break;
                            }
                            return false;
                        }
                    });
                }
            });
            tvFolderName.setText(file.getName());
            if (file.isDirectory()){
                imgFolder.setImageResource(R.drawable.ic_folder_black_32dp);
            }else{imgFolder.setImageResource(R.drawable.ic_file_black_32dp);}
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onFileEvenListener.onOpenFile(file);
                }
            });
        }

    }
    public interface OnFileEvenListener{
        void onOpenFile(File file);
        void onDeleteClicked(File file);
        void onMoveClicked(File file);
        void onCopyClicked(File file);
    }
}
