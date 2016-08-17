package com.example.administrator.myapplication.fileupload;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.administrator.myapplication.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/17.
 */

public class UploadFaceAdapter extends BaseAdapter {
    private  Context context;
    private  ArrayList<UploadFace> uploadFaces;
    private LayoutInflater inflater;
    private ImageLoader imageLoader;

    public UploadFaceAdapter() {
        super();
    }
    public UploadFaceAdapter(Context context, ArrayList<UploadFace> uploadFaces) {
        this.context=context;
        this.uploadFaces=uploadFaces;
    }

    @Override
    public int getCount() {
        return uploadFaces.size();
    }

    @Override
    public Object getItem(int i) {
        return uploadFaces.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (imageLoader == null)
            imageLoader =VolleyController.getInstance(context).getImageLoader();


        ViewHolder viewHolder=null;
        if (view==null){
            view = inflater.inflate(R.layout.uploadface_list_item, null);
            NetworkImageView niv= (NetworkImageView) view.findViewById(R.id.uploadface_image);
            TextView tv= (TextView) view.findViewById(R.id.uploadface_date);
            viewHolder=new ViewHolder();
            viewHolder.ivFace=niv;
            viewHolder.tvDate=tv;
            view.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) view.getTag();
        }
        UploadFace uploadFace=uploadFaces.get(i);
        viewHolder.ivFace.setImageUrl(Constant.UploadFaceThumbnailDirUrl+uploadFace.getThumbnailImgFileName(),imageLoader);
        viewHolder.tvDate.setText((CharSequence) uploadFace.getDate());
        return view;
    }
    class ViewHolder{
        NetworkImageView ivFace;
        TextView tvDate;
    }

}
