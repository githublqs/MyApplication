package com.example.administrator.myapplication.fileupload;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lqs on 2016/8/20.
 */

public class UriResolver {
    private static String selectImage(Context context, Uri  selectedImage){
        //Uri selectedImage = data.getData();
//      Log.e(TAG, selectedImage.toString());
        if(selectedImage!=null){
            String uriStr=selectedImage.toString();
            String path=uriStr.substring(10,uriStr.length());
            if(path.startsWith("com.sec.android.gallery3d")){
                //Log.e(TAG, "It's auto backup pic path:"+selectedImage.toString());
                return null;
            }
        }
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(selectedImage,filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }

    //如果是用"图库"来选，非缩略图,
    //cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
    //Permission Denial: reading com.android.providers.media.MediaProvider
    // uri content://media/external/images/media from pid=24921, uid=10056
    // requires android.permission.READ_EXTERNAL_STORAGE, or grantUriPermission()null);
    //所以需要加上权限READ_EXTERNAL_STORAGE
    public static String getPath(final Context context, final Uri uri) {
        if(uri==null){
            return null;
        }
        if("file".equals(uri.getScheme())){
            String path=uri.getPath();
            return  path;

        }


        //final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ) {
            if(DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;//
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }else{   //DocumentsContract.isDocumentUri(context, uri) 返回false的话,就用旧的方式
                return  selectImage(context,uri);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null){
                cursor.close();
            }
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    //String IMAGE_FILE_LOCATION = Environment.getExternalStorageDirectory()+"/temp.jpg";
    //intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);//直接输出文件,uri=intent.getData();//制定后牌照的原图片会存在这里了
    //intent.putExtra("return-data", true); //是否返回数据（照片剪切目的返回比较合适，缩略图目的） true bitmpa 不为空 否则为空 onActivityResult下 Bitmap bitmap = intent.getParcelableExtra("data");
	//Uri originalUri = data.getData();//获取原图,系统相册路径EXTERNAL_CONTENT_URI相册中


    //从相册截大图
    public static Intent getBigScaledImageFrompPotoAlbum(String whereToStore ){
        String IMAGE_FILE_LOCATION =whereToStore ;//temp file"file:///sdcard/temp.jpg"
        Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);//The Uri to store the big bitmap
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);

        intent.setType("image/*");

        intent.putExtra("crop", "true");

        intent.putExtra("aspectX", 2);

        intent.putExtra("aspectY", 1);

        intent.putExtra("outputX", 600);

        intent.putExtra("outputY", 300);

        intent.putExtra("scale", true);

        intent.putExtra("return-data", false);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        intent.putExtra("noFaceDetection", true); // no face detection
        return intent;

    }
    //从相册截小图
    public static Intent getSmallScaledImageFrompPotoAlbum(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

        intent.setType("image/*");

        intent.putExtra("crop", "true"); // 开启剪切

        intent.putExtra("aspectX", 1); // 剪切的宽高比为1：2

        intent.putExtra("aspectY", 2);

        intent.putExtra("outputX", 100);

        intent.putExtra("outputY", 200);

        intent.putExtra("scale", true);

        intent.putExtra("return-data", true);

        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        intent.putExtra("noFaceDetection", true); // no face detection
        return  intent;
    }


    //从相册截大图
    public static Intent getScaledImageFrompPotoAlbum(Uri uri,int aspectX,int aspectY ,int outputX,int outputY){

       /* Intent intent = new  Intent(
						Intent.ACTION_GET_CONTENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);*/



        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);

        intent.setType("image/*");

        intent.putExtra("crop", "true");

        intent.putExtra("aspectX", aspectX);

        intent.putExtra("aspectY", aspectY);

        intent.putExtra("outputX", outputX);

        intent.putExtra("outputY", outputY);

        intent.putExtra("scale", true);

        intent.putExtra("return-data", false);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        intent.putExtra("noFaceDetection", true); // no face detection
        return intent;

    }



    //截图，在 onActivityResult中拿到返回的数据（Uri），并将Uri传递给截图的程序，如拍照后
    public  static Intent getCropImageUri(Uri uri,int aspectX,int aspectY, int outputX, int outputY){
        // cropImageUri(imageUri, 800, 400, CROP_BIG_PICTURE); 大
        //cropImageUri(imageUri, 300, 150, CROP_SMALL_PICTURE);小
            Intent intent = new Intent("com.android.camera.action.CROP");

            intent.setDataAndType(uri, "image/*");

            intent.putExtra("crop", "true");

            intent.putExtra("aspectX", aspectX);

            intent.putExtra("aspectY", aspectY);

            intent.putExtra("outputX", outputX);

            intent.putExtra("outputY", outputY);

            intent.putExtra("scale", true);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

            intent.putExtra("return-data", false);

            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

            intent.putExtra("noFaceDetection", true); // no face detection
        return intent;

    }

    /*public final class PicIntentType{
        //从相册截大图：
        public static final int CHOOSE_BIG_PICTURE=0;
        //从相册截小图：
        public static final int CHOOSE_SMALL_PICTURE=1;
        //拍照截小图：
        public static final int CROP_SMALL_PICTURE=2;
        //拍照截大图：
        public static final int CROP_BIG_PICTURE=3;

    }*/

    public static Intent getImageOrCaptureIntent(Context context,ExtraOutputUriCallBack callBack,int what){
        Intent intent=null;
        switch (what){
            case  Constant.SELECT_PICTURE:
                 /*是调用系统图库来做,但是发现如果有图片是同步到google相册的话,
				图库里面能看到一个auto backup的目录,点进去选图片的话是无法获取到图片的路径的.
				因为那些图片根本就不存在于手机上.然后看到无论是百度贴吧,Instagram,
				或者还有些会选取图片做修改的app,都是用一个很漂亮的图片选择器(4.4以上,4.3的还是用系统旧的图库)
				测试结果:java.io.FileNotFoundException: No such file or directory（这里的错误也是因为权限READ_EXTERNAL_STORAGE 没加造成的）
				//content://media/external/images/media/26467
				InputStream in=context.getContentResolver().openInputStream(imageFileUri);
				*/
                intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");


                //而这个图片选择器可以屏蔽掉那个auto backup的目录.所以就开始打算用这个图片选择器来选图片了.
                //这个方法就是
                //4.3返回的是带文件路径的,而4.4返回的却是content://com.android.providers.media.documents/document/image:3951这样的,
                // 没有路径,只有图片编号的uri.这就导致接下来无法根据图片路径来裁剪的步骤了
                // .测试发现下面能返回Url content://com.android.providers.media.documents/document/image:3951
				/*Intent intent = new  Intent(
						Intent.ACTION_GET_CONTENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setType("image*//*");*/

                break;
            case Constant.SELECT_CAMER:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//action is capture 拍照
                //intent.addCategory(Intent.CATEGORY_DEFAULT);
                SimpleDateFormat timeStampFormat = new SimpleDateFormat(
                        "yyyy_MM_dd_HH_mm_ss");
                String filename = timeStampFormat.format(new Date());
                /*ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, filename);*/
                File parent=new File(Environment.getExternalStorageDirectory(),"uploadface");
                if(!parent.exists()){
                    parent.mkdirs();
                }
                File f=new File(parent, filename+".jpg");//localTempImgDir和localTempImageFileName是自己定义的名字
                Uri imageUri=Uri.fromFile(f);
               /* Uri imageUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);*/

                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                if(callBack!=null){
                    callBack.onPutExtra(imageUri);
                }

                break;
        }
        return intent;









    }
    public interface ExtraOutputUriCallBack{
        void onPutExtra(Uri imageUri);
    }
}
