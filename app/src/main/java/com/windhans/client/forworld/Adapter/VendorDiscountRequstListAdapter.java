package com.windhans.client.forworld.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.windhans.client.forworld.Model.MyDiscountRequest;
import com.windhans.client.forworld.Model.VendorDiscountRequest;
import com.windhans.client.forworld.R;
import com.windhans.client.forworld.my_library.Constants;
import com.windhans.client.forworld.my_library.DateTimeFormat;
import com.windhans.client.forworld.my_library.MyConfig;
import com.windhans.client.forworld.my_library.Shared_Preferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class VendorDiscountRequstListAdapter extends RecyclerView.Adapter<VendorDiscountRequstListAdapter.VisitHolder> {

    private List<VendorDiscountRequest> myDiscountRequestArrayList;

    private View itemView;
    private Context context;
    private ProgressDialog progressDialog;
    private String offerType;
    private Float salesCost;

    public class VisitHolder extends RecyclerView.ViewHolder {
        public TextView tv_requestId, tv_business_name,tv_requestAmount,tv_discountAmount,
                tv_product_name,tv_businessMobile,tv_status,tv_approval_date;
        private ImageView iv_delete_user;
        private Button btn_update;

        public VisitHolder(View view) {
            super(view);
            context = view.getContext();
            tv_requestId = (TextView)view.findViewById(R.id.tv_requestId);
            tv_business_name = (TextView)view.findViewById(R.id.tv_business_name);
            tv_requestAmount = (TextView)view.findViewById(R.id.tv_requestAmount);
            tv_discountAmount = (TextView)view.findViewById(R.id.tv_discountAmount);
            tv_product_name = (TextView)view.findViewById(R.id.tv_product_name);
            tv_businessMobile = (TextView)view.findViewById(R.id.tv_businessMobile);
            tv_status = (TextView)view.findViewById(R.id.tv_status);
            tv_approval_date = (TextView)view.findViewById(R.id.tv_approval_date);
            btn_update = (Button) view.findViewById(R.id.btn_update);

        }
    }

    public VendorDiscountRequstListAdapter(List<VendorDiscountRequest> myDiscountRequestArrayList) {
        this.myDiscountRequestArrayList = myDiscountRequestArrayList;
    }

    @Override
    public VendorDiscountRequstListAdapter.VisitHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_view_vendor_discount_request, parent, false);

        return new VendorDiscountRequstListAdapter.VisitHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(VendorDiscountRequstListAdapter.VisitHolder holder, final int position) {
        final VendorDiscountRequest vendorDiscountRequest = myDiscountRequestArrayList.get(position);


        holder.tv_requestId.setText(vendorDiscountRequest.getQr_request_id());
        holder.tv_business_name.setText(vendorDiscountRequest.getFirst_name());
        holder.tv_requestAmount.setText(Constants.rs+vendorDiscountRequest.getTotal_amount());
        if(vendorDiscountRequest.getDiscount_amount().equals("null"))
            holder.tv_discountAmount.setText("--");
        else
            holder.tv_discountAmount.setText(vendorDiscountRequest.getDiscount_amount());
        holder.tv_product_name.setText(vendorDiscountRequest.getProduct_title());
        holder.tv_businessMobile.setText(vendorDiscountRequest.getMobile());

        if(vendorDiscountRequest.getStatus().equals("1")) {
            holder.tv_status.setText("Completed");
            holder.tv_status.setTextColor(context.getResources().getColor(R.color.green_new));
            holder.btn_update.setVisibility(View.GONE);
        }
        else {
            holder.tv_status.setText("Pending");
            holder.tv_status.setTextColor(context.getResources().getColor(R.color.red_new));
            holder.btn_update.setVisibility(View.VISIBLE);
        }

        if(vendorDiscountRequest.getApproved_at().equals("null"))
            holder.tv_approval_date.setText("--");
        else
        holder.tv_approval_date.setText( DateTimeFormat.getDate1_9(vendorDiscountRequest.getApproved_at()));

        holder.btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewDialogue(vendorDiscountRequest.getQr_request_id(),
                        vendorDiscountRequest.getTotal_amount());
            }
        });

    }

    @Override
    public int getItemCount() {
        return myDiscountRequestArrayList.size();
    }

    private void ViewDialogue(String qr_request_id, String totalAmount) {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_update_user_request);
        dialog.setCancelable(true);

        final TextInputEditText edt_discount_amount, edt_sales_cost,
                edt_original_cost, edt_how_much;
        final Spinner spinner_offer_type;



        edt_discount_amount = dialog.findViewById(R.id.edt_discount_amount);

        edt_sales_cost = dialog.findViewById(R.id.edt_sales_cost);
        edt_original_cost = dialog.findViewById(R.id.edt_original_cost);
        edt_how_much = dialog.findViewById(R.id.edt_how_much);
        spinner_offer_type = dialog.findViewById(R.id.spinner_offer_type);

        edt_original_cost.setText(""+totalAmount);

        spinner_offer_type.setSelection(1);
        offerType = "1";

        spinner_offer_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextSize(14);
                if (position == 1) {
                    offerType = "1"; // flat discount
                    edt_sales_cost.setText("");
                    edt_how_much.setText("");
                }
                else if (position == 2) {
                    offerType = "2"; // percentage discount
                    edt_sales_cost.setText("");
                    edt_how_much.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        edt_original_cost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(offerType.equals("1")){
                    edt_sales_cost.setText(edt_original_cost.getText().toString());
                }
            }
        });


        edt_how_much.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!edt_how_much.getText().toString().isEmpty() && !edt_original_cost.getText().toString().isEmpty()) {
                    if (offerType.equals("1")) {
                        salesCost = Float.valueOf(edt_original_cost.getText().toString()) - Float.parseFloat(edt_how_much.getText().toString());

                    } else  if (offerType.equals("2")){
                        salesCost = Float.valueOf(edt_original_cost.getText().toString()) * (Float.parseFloat(edt_how_much.getText().toString()) / 100);
                        salesCost = Float.valueOf(edt_original_cost.getText().toString()) - salesCost;
                    }
                    edt_sales_cost.setText(""+salesCost);
                }else {

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        dialog.findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("values", "onClick: "+edt_discount_amount+" "+qr_request_id+" "+totalAmount);
                updateDiscount(String.valueOf(salesCost),qr_request_id,totalAmount);
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().getAttributes().windowAnimations = R.style.MaterialDialog; //style id
        dialog.show();
    }

    public interface CRUDAPI {
        @FormUrlEncoded
        @POST(MyConfig.SSWORLD + "/update_Qr_request_by_vendor")
        Call<ResponseBody> checkTYPE(
                @Field("user_id") String user_id,
                @Field("qr_request_id") String qr_request_id,
                @Field("discount_amount") String discount_amount
        );
    }

    private void updateDiscount(String discountAmount, String qr_request_id, String totalAmount) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(context.getResources().getString(R.string.loading));
        //progressDialog.setCancelable(true);
        progressDialog.setCancelable(false);
        progressDialog.show();


        CRUDAPI api = MyConfig.getRetrofit(MyConfig.JSON_BASE_URL).create(CRUDAPI.class);
        Call<ResponseBody> result = (Call<ResponseBody>) api.checkTYPE(
                Shared_Preferences.getPrefs(context, Constants.REG_ID),
                qr_request_id,
                discountAmount

        );
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String output = response.body().string();

                    Log.d("user_id", "response=> " + output);
                    Log.d("Response", "response=> " + output);
                    JSONObject jsonObject = new JSONObject(output);
                    boolean res = Boolean.parseBoolean(jsonObject.getString("result"));
                    String reason = jsonObject.getString("reason");

                    if (res) {
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, reason, Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e1) {
                    e1.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Log.d("Retrofit Error:",t.getMessage());
                //progressDialog.dismiss();
            }
        });
    }

}

