package com.example.administrator.myapplication.fileupload;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.baoyz.swipemenulistview.BaseSwipListAdapter;
import com.example.administrator.myapplication.R;
import java.util.ArrayList;
public class UploadFaceAdapter extends BaseSwipListAdapter{
    private  Context context;
    private  ArrayList<UploadFace> uploadFaces;
    private LayoutInflater inflater;
    public ArrayList<UploadFace> getUploadFaces() {
        return uploadFaces;
    }
    private ImageLoader imageLoader;

   public UploadFaceAdapter(Context context, ArrayList<UploadFace> uploadFaces) {
        this.context=context;
        this.uploadFaces=uploadFaces;

   }
    public void remove(UploadFace uploadFace){
        uploadFaces.remove(uploadFace);
    }
    @Override
    public boolean getSwipEnableByPosition(int position) {
        /*if(position % 2 == 0){
            return false;
        }*/
        return true;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (imageLoader == null)
            imageLoader =VolleyController.getInstance(context).getImageLoader();
        ViewHolder vh=null;
        if (convertView==null){
            vh=new ViewHolder();
            convertView = inflater.inflate(R.layout.uploadface_list_item,
                    parent, false);
            NetworkImageView niv= (NetworkImageView) convertView.findViewById(R.id.uploadface_image);
            TextView tv= (TextView) convertView.findViewById(R.id.uploadface_date);
            TextView tvId= (TextView) convertView.findViewById(R.id.uploadface_id);
            vh.niv=niv;
            vh.tvDate=tv;
            vh.tvId=tvId;
            convertView.setTag(vh);
        }else {
            vh= (ViewHolder) convertView.getTag();
        }

        UploadFace uploadFace=uploadFaces.get(position);
        vh.niv.setImageResource(R.mipmap.publicloading);
        vh.niv.setImageUrl(Constant.UploadFaceThumbnailDirUrl+uploadFace.getThumbnailImgFileName(),imageLoader);
        vh.tvDate.setText((CharSequence) uploadFace.getDate());
        vh.tvId.setText(uploadFace.getId()+"");
        return convertView;

    }
    class ViewHolder{
        NetworkImageView niv;
        TextView tvDate;
        public TextView tvId;
    }
}
