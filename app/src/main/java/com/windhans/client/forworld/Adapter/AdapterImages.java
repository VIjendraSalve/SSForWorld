package com.windhans.client.forworld.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.windhans.client.forworld.Model.ImagePOJO;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.MyConfig;
import com.windhans.client.forworld.my_library.Shared_Preferences;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import static com.windhans.client.forworld.my_library.Constants.loadImage;



/**
 * Created by wind Prasanna on 11/1/2017.
 */

public class AdapterImages extends RecyclerView.Adapter<AdapterImages.ImageViewHolder> {

    private Context context;
    private int IMG_TYPE;
    private List<ImagePOJO> data = new ArrayList<>();
    private ProgressDialog progressDialog;
    private int requirement_id;
    private Call<ResponseBody> result;

    public AdapterImages(Context context, List<ImagePOJO> data, int IMG_TYPE, int requirement_id) {
        this.context = context;
        this.IMG_TYPE = IMG_TYPE;
        this.data = data;
        this.requirement_id = requirement_id;
        //  this.DOC_TYPE = DOC_TYPE;
        //  this.doc_name = doc_name;
        //  this.id = id;
    }

    /*public AdapterImages(Context context, int IMG_TYPE, String IMG_URL, List<ImagePOJO> data, boolean isShow) {
        this.context = context;
        this.IMG_TYPE = IMG_TYPE;
        this.IMG_URL = IMG_URL;
        this.data = data;
        this.isShow = true;
    }*/

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_image_row, parent, false));
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, final int position) {
        final ImagePOJO current = data.get(position);
        Log.d("my_tag", "onBindViewHolder: " + position);
        if (!current.getImg_path_local().equalsIgnoreCase(""))
            loadImage(context, current.getImg_path_local(), holder.iv_tag_img, R.mipmap.ic_launcher_round);
        else
            loadImage(context, current.getImg_name(), holder.iv_tag_img, R.mipmap.ic_launcher_round);

     /*   Glide.with(context).load(current.getImg_path_local())
                .into(holder.iv_tag_img);*/

        holder.imgShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!current.getImg_path_local().equalsIgnoreCase(""))
                    Constants.showDocument(context, current.getImg_path_local());
                else
                    Constants.showDocument(context, current.getImg_name());
            }
        });

        holder.imgUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ImageUpdate) context).onUpdateImage(IMG_TYPE, position, holder.iv_tag_img);
                notifyItemChanged(position);
            }
        });

        holder.imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!current.getImg_id().equalsIgnoreCase("0"))
                    remove_alert(position);
                else {
                    notifyItemRemoved(position);
                    data.remove(position);
                    notifyDataSetChanged();
                }
            }
        });

        if (IMG_TYPE == 1) {
            holder.imgUpdate.setVisibility(View.GONE);
            holder.imgRemove.setVisibility(View.GONE);
        }
    }

    private void remove_alert(final int position1) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Remove Room Image");
        alertDialog.setMessage("Are You Sure You Want to Remove this Room Image?");
        alertDialog.setIcon(android.R.drawable.ic_delete);
        alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                //remove(position1);
            }
        });

        alertDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    public void remove(final int position) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Removing Image Please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
        Map<String, String> params = new HashMap<>();
        RemoveAPI api = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(RemoveAPI.class);

        if (data.get(position).getImage_type() == Constants.ROOM_IMAGE) {
            result = api.remove_room_image(params);
            params.put("pg_requirement_id", "" + requirement_id);
            params.put("requirement_image_id", data.get(position).getImg_id());
        } else if (data.get(position).getImage_type() == Constants.ROOM_MEMBER_DOC_IMAGE) {
            result = api.remove_member_doc(params);
            params.put("requirement_user_id", "" + data.get(position).getPg_requirement_id());
            params.put("user_documents_id", "" + data.get(position).getImg_id());
        } else if (data.get(position).getImage_type() == Constants.PROFILE_DOC_IMAGE) {
            result = api.remove_profile_doc(params);
            params.put("pg_signup_id", "" + Shared_Preferences.getPrefs(context, Constants.REG_ID));
            params.put("user_profile_documents_id", "" + data.get(position).getImg_id());
        }


        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";

                try {
                    output = response.body().string();
                    Log.d("my_tag", "onResponse: " + output);
                    JSONObject ob = new JSONObject(output);
                    boolean res = ob.getBoolean("result");
                    String reason = ob.getString("reason");

                    if (res) {

                        if (data.get(position).getImage_type() == Constants.PROFILE_DOC_IMAGE) {
                            JSONArray jsonArray = new JSONArray(Shared_Preferences.getPrefs(context, Constants.SIGNUP_PROFILE_DOCUMENT_IMAGES));
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                jsonArray.remove(position);
                            }
                            Shared_Preferences.setPrefs(context, Constants.SIGNUP_PROFILE_DOCUMENT_IMAGES, jsonArray.toString());
                        }
                        notifyItemRemoved(position);
                        data.remove(position);
                        Toast.makeText(context, reason, Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();

                    } else {
                        Toast.makeText(context, reason, Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
            }
        });

    }

    public interface RemoveAPI {

        //delete room image
        @FormUrlEncoded
        @POST(MyConfig.JSON_BASE_URL + "/delete_requirement_image")
        public Call<ResponseBody> remove_room_image(
                @FieldMap() Map<String, String> params
        );

        //delete room assign user document image
        @FormUrlEncoded
        @POST(MyConfig.JSON_BASE_URL + "/room_assigned_user_document_delete")
        public Call<ResponseBody> remove_member_doc(
                @FieldMap() Map<String, String> params
        );

        //delete
        @FormUrlEncoded
        @POST(MyConfig.JSON_BASE_URL + "/delete_user_profile_document_image")
        public Call<ResponseBody> remove_profile_doc(
                @FieldMap() Map<String, String> params
        );
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgRemove, imgUpdate, imgShow,iv_tag_img;

        public ImageViewHolder(View itemView) {
            super(itemView);
            iv_tag_img = itemView.findViewById(R.id.iv_tag_img);
            imgRemove = (ImageView) itemView.findViewById(R.id.imgRemove);
            imgUpdate = (ImageView) itemView.findViewById(R.id.imgUpdate);
            imgShow = (ImageView) itemView.findViewById(R.id.imgShow);
        }
    }

    public interface ImageUpdate {
        void onUpdateImage(int IMG_TYPE, int position, ImageView imageView);
    }
}
