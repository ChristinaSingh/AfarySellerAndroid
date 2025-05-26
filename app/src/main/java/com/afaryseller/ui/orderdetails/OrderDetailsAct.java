package com.afaryseller.ui.orderdetails;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;

import com.afaryseller.R;
import com.afaryseller.core.BaseActivity;
import com.afaryseller.databinding.ActivityOrderDetailsBinding;
import com.afaryseller.retrofit.ApiConstant;
import com.afaryseller.retrofit.Constant;
import com.afaryseller.ui.chat.ChatAct;
import com.afaryseller.ui.editproduct.EditProductAct;
import com.afaryseller.ui.subscription.SubscriptionAct;
import com.afaryseller.utility.DataManager;
import com.afaryseller.utility.SessionManager;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Part;


public class OrderDetailsAct extends BaseActivity<ActivityOrderDetailsBinding, OrderDetailsViewModel> implements OrderItemListener {
    ActivityOrderDetailsBinding binding;
    OrderDetailsViewModel orderDetailsViewModel;
    String orderId = "", orderStatus = "", token = "", type = "",selectCheck="";
    OrderDetailsModel model;
    //  double totalPriceToToPay=0.0,taxN1=0.0,taxN2=0.0,platFormsFees=0.0,deliveryFees=0.0,subTotal=0.0;
    String totalPriceToToPay = "0", taxN1 = "0", taxN2 = "0", platFormsFees = "0", deliveryFees = "0", subTotal = "0", reason = "";

    SessionManager sessionManager;
    ItemsAdapter itemsAdapter;
    ArrayList<OrderDetailsModel.Result.Product> arrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_details);
        orderDetailsViewModel = new OrderDetailsViewModel();
        binding.setOrderDetailsViewModel(orderDetailsViewModel);
        binding.getLifecycleOwner();
        orderDetailsViewModel.init(OrderDetailsAct.this);
        sessionManager = new SessionManager();
        initViews();


    }

    private void initViews() {

        if (getIntent() != null) {
            orderId = getIntent().getStringExtra("id");

        }

        arrayList = new ArrayList<>();


        itemsAdapter = new ItemsAdapter(OrderDetailsAct.this, arrayList, OrderDetailsAct.this);
        binding.rvDetails.setAdapter(itemsAdapter);

        observeLoader();
        observeResponse();
        callOrderDetail();

        binding.backNavigation.setOnClickListener(v -> onBackPressed());

        binding.btnAccept.setOnClickListener(v ->
        {

            if (binding.btnAccept.getText().equals("Transit")) {
                callToDeliveryTransit("In_Transit");
            }

            if (binding.btnAccept.getText().equals("Delivered")) {
                callToDeliveryTransit("Reached_shipping_company");
            } else {
                if (model != null) {
                    if (model.getResult().getStatus().equals("Pending")) {
                        orderStatus = "Accepted";
                        type = "All";
                        if (model.getResult().getSelfCollect().equals("No"))
                            /* callAssignDelivery(); */
                            callAcceptDeclineOrder(orderStatus, type, orderId, "");
                        else callAcceptDeclineOrder(orderStatus, type, orderId, "");

                    } else if (model.getResult().getStatus().equals("Accepted_by_admin")) {
                        orderStatus = "Accepted";
                        type = "All";
                        //  callAssignDelivery();
                        callAcceptDeclineOrder(orderStatus, type, orderId, "");


                    } else if (model.getResult().getSelfCollect().equals("Yes")) {
                        dialogAfaryCode();
                    }


/*
                    else {
                        SessionManager.writeString(OrderDetailsAct.this,"afaryCode",model.getResult().getAfaryCode());
                        SessionManager.writeString(OrderDetailsAct.this,"orderId",model.getResult().getDeliveryPerson().getOrderId());
                        startActivity(new Intent(OrderDetailsAct.this,OrderTrackAct.class)
                                .putExtra("orderDetails",model.getResult()));
                    }
*/
                }

            }


        });

        callTokenAcc();

        binding.btnDecline.setOnClickListener(v -> {
            orderStatus = "Cancelled";
            type = "All";
            // alertCancelOrder("Are you sure you want to refuse All order?",orderStatus,"All",orderId);
            if (model.getResult().getStatus().equalsIgnoreCase("Pending") || model.getResult().getStatus().equalsIgnoreCase("Accepted") ||
                    model.getResult().getStatus().equalsIgnoreCase("PickedUp"))
                dialogSelectCancelOrderReason(orderStatus, "All", orderId);
        });

        binding.btnChat.setOnClickListener(v -> {
            // if(model.getResult().getStatus().equals("Accept")){
            startActivity(new Intent(this, ChatAct.class)
                    .putExtra("UserId", model.getResult().getUserId())
                    .putExtra("UserName", model.getResult().getUserName())
                    .putExtra("UserImage", model.getResult().getUserImage()));
            //   }
        });


    }


    private void callToDeliveryTransit(String modeType) {
        HashMap<String, String> map = new HashMap<>();
        map.put("common_order_id", orderId);
        map.put("status", modeType);
        map.put("user_seller_id", DataManager.getInstance().getUserData(OrderDetailsAct.this).getResult().getId());
        map.put("seller_register_id", DataManager.getInstance().getUserData(OrderDetailsAct.this).getResult().getRegisterId());
        orderDetailsViewModel.internationalDeliveryTransit(OrderDetailsAct.this, map);
    }


    private void dialogAfaryCode() {

        Dialog mDialog = new Dialog(OrderDetailsAct.this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_afarycode);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);

        EditText edAfaryCode = mDialog.findViewById(R.id.edAfaryCode);
        AppCompatButton btnCancel = mDialog.findViewById(R.id.btnCancel);
        AppCompatButton btnOk = mDialog.findViewById(R.id.btnOk);

        btnCancel.setOnClickListener(v -> {
            mDialog.dismiss();

        });

        btnOk.setOnClickListener(v -> {
            if (edAfaryCode.getText().toString().equals(""))
                Toast.makeText(OrderDetailsAct.this, getString(R.string.please_enter_afary_code), Toast.LENGTH_SHORT).show();

            else {
                mDialog.dismiss();
                callCompleteSelfCollect(edAfaryCode.getText().toString());
            }

        });
        mDialog.show();
    }

    private void alertCancelOrder(String msg, String orderStatus, String type, String orderId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailsAct.this);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        // callAcceptDeclineOrder(orderStatus,type,orderId);
                    }
                }).setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }

    private void callAssignDelivery() {
        MultipartBody.Part filePart = null;
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + token);
        headerMap.put("Accept", "application/json");

        if (!model.getResult().getProductList().get(0).getImage1().equalsIgnoreCase("")) {
            // File file =   new File("")  ; // DataManager.getInstance().saveBitmapToFile(new File(str_image_path));
            // str_image_path = DataManager.compressImage(getActivity(),str_image_path);
            //File file = new File(str_image_path);
            //filePart = MultipartBody.Part.createFormData("package_picture", file.getName(), RequestBody.create(MediaType.parse("package_picture/*"), file));
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), model.getResult().getProductList().get(0).getImage1());
            filePart = MultipartBody.Part.createFormData("package_picture", "", attachmentEmpty);
        }/* else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
            filePart = MultipartBody.Part.createFormData("package_picture", "", attachmentEmpty);
        }*/


        RequestBody package_name = RequestBody.create(MediaType.parse("text/plain"), model.getResult().getProductList().get(0).getProductName());
        RequestBody owner = RequestBody.create(MediaType.parse("text/plain"), model.getResult().getUserName());
        RequestBody command_date = RequestBody.create(MediaType.parse("text/plain"), "2022-03-02");
        RequestBody adresse_source = RequestBody.create(MediaType.parse("text/plain"), model.getResult().getProductList().get(0).getShopAddress());
        //        RequestBody adresse_source = RequestBody.create(MediaType.parse("text/plain"),"");
        RequestBody adresse_destination;
        if (model.getResult().getSelfCollect().equals("No")) {
            adresse_destination = RequestBody.create(MediaType.parse("text/plain"), model.getResult().getAddress());
        } else {
            adresse_destination = RequestBody.create(MediaType.parse("text/plain"), "");
        }

        // RequestBody adresse_destination = RequestBody.create(MediaType.parse("text/plain"),"");
        RequestBody delivery_type = RequestBody.create(MediaType.parse("text/plain"), "Privé");
        RequestBody latitude_source = RequestBody.create(MediaType.parse("text/plain"), model.getResult().getProductList().get(0).getShopLat());
        //  RequestBody latitude_source = RequestBody.create(MediaType.parse("text/plain"),"");
        RequestBody longitude_source = RequestBody.create(MediaType.parse("text/plain"), model.getResult().getProductList().get(0).getShopLon());
        //  RequestBody longitude_source = RequestBody.create(MediaType.parse("text/plain"),"");
        RequestBody latitude_destination = RequestBody.create(MediaType.parse("text/plain"), model.getResult().getDeliverLat());
        //  RequestBody latitude_destination = RequestBody.create(MediaType.parse("text/plain"),"");
        RequestBody longitude_destination = RequestBody.create(MediaType.parse("text/plain"), model.getResult().getDeliverLon());
        //  RequestBody longitude_destination = RequestBody.create(MediaType.parse("text/plain"),"");
        RequestBody command_number = RequestBody.create(MediaType.parse("text/plain"), model.getResult().getOrderId());
        RequestBody delivery_amount = RequestBody.create(MediaType.parse("text/plain"), parseFrenchNumber(model.getResult().getDeliveryCharges()) + "");
        RequestBody command_amount = RequestBody.create(MediaType.parse("text/plain"), parseFrenchNumber(model.getResult().getTotalAmount()) + "");
        RequestBody customer_code = RequestBody.create(MediaType.parse("text/plain"), model.getResult().getAfaryCode());

        orderDetailsViewModel.addDeliveryToken(OrderDetailsAct.this, headerMap, package_name, owner, command_date, adresse_source, adresse_destination, delivery_type,
                latitude_source, longitude_source, latitude_destination, longitude_destination, command_number, delivery_amount, command_amount, customer_code, filePart);


        Map<String, String> map = new HashMap<>();
        map.put("package_name", model.getResult().getProductList().get(0).getProductName());
        map.put("owner", model.getResult().getUserName());
        map.put("command_date", DataManager.currentDate());
        map.put("adresse_source", model.getResult().getProductList().get(0).getShopAddress());
        map.put("adresse_destination", model.getResult().getAddress());
        map.put("delivery_type", "Privé");
        map.put("latitude_source", model.getResult().getProductList().get(0).getShopLat());
        map.put("longitude_source", model.getResult().getProductList().get(0).getShopLon());
        map.put("latitude_destination", model.getResult().getDeliverLat());
        map.put("longitude_destination", model.getResult().getDeliverLon());
        map.put("command_number", model.getResult().getOrderId());
        map.put("delivery_amount", parseFrenchNumber(model.getResult().getDeliveryCharges()) + "");
        map.put("command_amount", parseFrenchNumber(model.getResult().getTotalAmount()) + "");
        map.put("customer_code", model.getResult().getAfaryCode());

        Log.e("Delivery assign====", map.toString());

    }


    private void callOrderDetail() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + DataManager.getInstance().getUserData(OrderDetailsAct.this).getResult().getAccessToken());
        headerMap.put("Accept", "application/json");


        HashMap<String, String> map = new HashMap<>();
        map.put("order_id", orderId);
        map.put("user_seller_id", DataManager.getInstance().getUserData(OrderDetailsAct.this).getResult().getId());
        map.put("seller_register_id", DataManager.getInstance().getUserData(OrderDetailsAct.this).getResult().getRegisterId());

        map.put("sub_seller_id", DataManager.getInstance().getUserData(OrderDetailsAct.this).getResult().getSub_seller_id());
        map.put("shop_id", SessionManager.readString(OrderDetailsAct.this, Constant.shopId, ""));
        orderDetailsViewModel.getOrderDetails(OrderDetailsAct.this, headerMap, map);
    }


    private void callCompleteSelfCollect(String afaryCode) {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + DataManager.getInstance().getUserData(OrderDetailsAct.this).getResult().getAccessToken());
        headerMap.put("Accept", "application/json");


        HashMap<String, String> map = new HashMap<>();
        map.put("order_id", orderId);
        map.put("customer_afary_code", afaryCode);
        map.put("user_id", DataManager.getInstance().getUserData(OrderDetailsAct.this).getResult().getId());
        // map.put("seller_register_id",DataManager.getInstance().getUserData(OrderDetailsAct.this).getResult().getRegisterId());
        orderDetailsViewModel.completeSelfCollectOrder(OrderDetailsAct.this, headerMap, map);
    }


    private void callTokenAcc() {
        HashMap<String, String> map = new HashMap<>();
        map.put("login", "test");
        map.put("password", "1234567890");
        orderDetailsViewModel.getToken(OrderDetailsAct.this, map);
    }

    private void callAcceptDeclineOrder(String status, String type, String orderId, String reason) {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + DataManager.getInstance().getUserData(OrderDetailsAct.this).getResult().getAccessToken());
        headerMap.put("Accept", "application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("seller_id", DataManager.getInstance().getUserData(OrderDetailsAct.this).getResult().getId());
        map.put("user_id", model.getResult().getUserId());
        map.put("order_id", orderId);
        map.put("status", status);
        map.put("type", type);
        map.put("reason", reason);
        map.put("user_seller_id", DataManager.getInstance().getUserData(OrderDetailsAct.this).getResult().getId());
        map.put("seller_register_id", DataManager.getInstance().getUserData(OrderDetailsAct.this).getResult().getRegisterId());
        if (DataManager.getInstance().getUserData(OrderDetailsAct.this).getResult().getType().equals(Constant.SUBADMIN))
        map.put("sub_seller_id", "");
        else  map.put("sub_seller_id", DataManager.getInstance().getUserData(OrderDetailsAct.this).getResult().getSub_seller_id());


        Log.e("orderAcceptDecline====", map.toString());
        orderDetailsViewModel.acceptDeclineOrder(OrderDetailsAct.this, headerMap, map);
    }


    public void observeResponse() {
        orderDetailsViewModel.isResponse.observe(this, dynamicResponseModel -> {

            if (dynamicResponseModel.getJsonObject() != null) {
                pauseProgressDialog();
                if (dynamicResponseModel.getApiName() == ApiConstant.GET_ORDER_DETAIL) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                // binding.tvNotFount.setVisibility(View.GONE);
                                model = new Gson().fromJson(stringResponse, OrderDetailsModel.class);
                                binding.tvAfaryCode.setText(model.getResult().getAfaryCode());
                                //  binding.tvAfaryCode.setText(model.getResult().getOrderId());

                                //  Glide.with(OrderDetailsAct.this).load(model.getResult().getProductList().get(0).getProductImages()).into(binding.productImg);
                                binding.tvShopName.setText(model.getResult().getProductList().get(0).getShopName());
                                binding.tvOrderNumber.setText(orderId + "");
                              //  binding.tvDate.setText(model.getResult().getProductList().get(0).getDateTime());
                                binding.tvDate.setText(model.getResult().getLocal_date_time());


                                if (model.getResult().getProductList().size() == 1) {
                                    binding.llOne.setVisibility(View.VISIBLE);
                                    binding.llTwo.setVisibility(View.GONE);
                                    binding.llThree.setVisibility(View.GONE);
                                    binding.llFour.setVisibility(View.GONE);
                                    Glide.with(OrderDetailsAct.this).load(model.getResult().getProductList().get(0).getProductImages()).into(binding.productImage);

                                } else if (model.getResult().getProductList().size() == 2) {
                                    binding.llOne.setVisibility(View.GONE);
                                    binding.llTwo.setVisibility(View.VISIBLE);
                                    binding.llThree.setVisibility(View.GONE);
                                    binding.llFour.setVisibility(View.GONE);
                                    Glide.with(OrderDetailsAct.this).load(model.getResult().getProductList().get(0).getProductImages()).into(binding.productImageTw01);
                                    Glide.with(OrderDetailsAct.this).load(model.getResult().getProductList().get(1).getProductImages()).into(binding.productImageTwo2);

                                } else if (model.getResult().getProductList().size() == 3) {
                                    binding.llOne.setVisibility(View.GONE);
                                    binding.llTwo.setVisibility(View.GONE);
                                    binding.llThree.setVisibility(View.VISIBLE);
                                    binding.llFour.setVisibility(View.GONE);
                                    Glide.with(OrderDetailsAct.this).load(model.getResult().getProductList().get(0).getProductImages()).into(binding.productImageThree1);
                                    Glide.with(OrderDetailsAct.this).load(model.getResult().getProductList().get(1).getProductImages()).into(binding.productImageThree2);
                                    Glide.with(OrderDetailsAct.this).load(model.getResult().getProductList().get(2).getProductImages()).into(binding.productImageThree3);

                                } else if (model.getResult().getProductList().size() <= 4) {
                                    binding.llOne.setVisibility(View.GONE);
                                    binding.llTwo.setVisibility(View.GONE);
                                    binding.llThree.setVisibility(View.GONE);
                                    binding.llFour.setVisibility(View.VISIBLE);
                                    Glide.with(OrderDetailsAct.this).load(model.getResult().getProductList().get(0).getProductImages()).into(binding.productImageFour1);
                                    Glide.with(OrderDetailsAct.this).load(model.getResult().getProductList().get(1).getProductImages()).into(binding.productImageFour2);
                                    Glide.with(OrderDetailsAct.this).load(model.getResult().getProductList().get(2).getProductImages()).into(binding.productImageFour3);
                                    binding.tvImgCount.setText("+" + (model.getResult().getProductList().size() - 3));

                                }


                                taxN1 = model.getResult().getTaxN1();
                                taxN2 = model.getResult().getTaxN2();
                                platFormsFees = model.getResult().getPlatFormsFees();
                                deliveryFees = model.getResult().getDeliveryCharges();
                                totalPriceToToPay = model.getResult().getTotalAmount();
                                int n1 = parseFrenchNumber(totalPriceToToPay);
                                int n2 = parseFrenchNumber(taxN1) + parseFrenchNumber(taxN2) + parseFrenchNumber(platFormsFees) + parseFrenchNumber(deliveryFees);
                                int n3 = n1 - n2;
                                subTotal = n3 + ""


                                ;

                                //   subTotal =  Double.parseDouble(model.getResult().getPrice());  // - deliveryFees;

                                //  totalPriceToToPay = Double.parseDouble(model.getResult().getPrice())
                                //            + Double.parseDouble(model.getResult().getPlatFormsFees())
                                //            + Double.parseDouble(model.getResult().getDeliveryCharges())
                                //           + Double.parseDouble(model.getResult().getTaxN1())
                                //           + Double.parseDouble(model.getResult().getTaxN2());


                                binding.plateformFees.setText("FCFA" + platFormsFees);
                                binding.tvTax1.setText("FCFA" + taxN1);
                                binding.tvtax2.setText("FCFA" + taxN2);
                                binding.tvDelivery.setText("FCFA" + deliveryFees);
                                //  binding.tvTotalAmt.setText("XAF" + String.format("%.2f", totalPriceToToPay));
                                binding.tvTotalAmt.setText("FCFA" + subTotal);
                                binding.subTotal.setText("FCFA" + subTotal);



                            /*    if(deliveryFees==0.00) {
                                    binding.rlDelivery.setVisibility(View.GONE);
                                }
                                else  {
                                    binding.rlDelivery.setVisibility(View.VISIBLE);
                                }*/

                                if (model.getResult().getDeliveryType().equals("INTERNATIONAL")) {

                                    if (model.getResult().getStatus().equals("Pending")) {
                                        binding.llButtons.setVisibility(View.VISIBLE);
                                        binding.btnAccept.setText(getString(R.string.accept));
                                        binding.tvAfaryCode.setVisibility(View.GONE);
                                        binding.rlDeliveryPerson.setVisibility(View.GONE);

                                    }

                                    if (model.getResult().getStatus().equals("Accepted_by_admin")) {
                                        binding.llButtons.setVisibility(View.VISIBLE);
                                        binding.btnAccept.setText(getString(R.string.accept));
                                        binding.tvAfaryCode.setVisibility(View.GONE);
                                        binding.rlDeliveryPerson.setVisibility(View.GONE);

                                    } else if (model.getResult().getStatus().equals("Accepted")) {
                                        binding.llButtons.setVisibility(View.VISIBLE);
                                        binding.btnAccept.setVisibility(View.VISIBLE);
                                        binding.btnAccept.setText(getString(R.string.transit));
                                        binding.btnChat.setVisibility(View.VISIBLE);
                                        binding.btnDecline.setVisibility(View.VISIBLE);
                                        binding.tvAfaryCode.setVisibility(View.GONE);
                                        binding.rlDeliveryPerson.setVisibility(View.GONE);


                                        //  binding.llButtons.setVisibility(View.VISIBLE);
                                        //   binding.btnAccept.setVisibility(View.GONE);
                                        //  binding.tvAfaryCode.setVisibility(View.VISIBLE);
                                        //   binding.rlDeliveryPerson.setVisibility(View.VISIBLE);


                                    } else if (model.getResult().getStatus().equals("In_Transit")) {
                                        binding.llButtons.setVisibility(View.VISIBLE);
                                        binding.btnAccept.setVisibility(View.VISIBLE);
                                        binding.btnAccept.setText(getString(R.string.delivered));
                                        binding.btnChat.setVisibility(View.GONE);
                                        binding.btnDecline.setVisibility(View.GONE);
                                        binding.tvAfaryCode.setVisibility(View.VISIBLE);
                                        binding.rlDeliveryPerson.setVisibility(View.GONE);


                                        //  binding.llButtons.setVisibility(View.VISIBLE);
                                        //   binding.btnAccept.setVisibility(View.GONE);
                                        //  binding.tvAfaryCode.setVisibility(View.VISIBLE);
                                        //   binding.rlDeliveryPerson.setVisibility(View.VISIBLE);


                                    } else if (model.getResult().getStatus().equals("Reached_shipping_company")) {
                                        binding.llButtons.setVisibility(View.GONE);
                                        binding.tvAfaryCode.setVisibility(View.VISIBLE);
                                        binding.rlDeliveryPerson.setVisibility(View.GONE);

                                    } else if (model.getResult().getStatus().equals("Cancelled")) {
                                        binding.llButtons.setVisibility(View.VISIBLE);
                                        binding.tvAfaryCode.setVisibility(View.GONE);

                                        binding.btnAccept.setVisibility(View.GONE);
                                        binding.btnChat.setVisibility(View.GONE);
                                        binding.btnDecline.setVisibility(View.VISIBLE);
                                        binding.btnDecline.setText(getString(R.string.cancelled));


                                    } else if (model.getResult().getStatus().equals("Cancelled_by_user")) {
                                        binding.llButtons.setVisibility(View.VISIBLE);
                                        binding.tvAfaryCode.setVisibility(View.GONE);

                                        binding.btnAccept.setVisibility(View.GONE);
                                        binding.btnChat.setVisibility(View.GONE);
                                        binding.btnDecline.setVisibility(View.VISIBLE);
                                        binding.btnDecline.setText(getString(R.string.cancelled));

                                    }

                                    arrayList.clear();
                                    arrayList.addAll(model.getResult().getProductList());
                                    for (int i = 0; i < arrayList.size(); i++) {
                                        if (model.getResult().getStatus().equals("In_Transit")) {
                                            arrayList.get(i).setStatus("In_Transit");
                                        } else if (model.getResult().getStatus().equals("Reached_shipping_company")) {
                                            arrayList.get(i).setStatus("Reached_shipping_company");
                                        }
                                    }
                                    itemsAdapter.notifyDataSetChanged();

                                } else {
                                    if (model.getResult().getStatus().equals("Pending")) {
                                        binding.llButtons.setVisibility(View.VISIBLE);
                                        binding.btnAccept.setText(getString(R.string.accept));
                                        binding.tvAfaryCode.setVisibility(View.GONE);
                                        binding.rlDeliveryPerson.setVisibility(View.GONE);

                                    }

                                    if (model.getResult().getStatus().equals("Accepted_by_admin")) {
                                        binding.llButtons.setVisibility(View.VISIBLE);
                                        binding.btnAccept.setText(getString(R.string.accept));
                                        binding.tvAfaryCode.setVisibility(View.GONE);
                                        binding.rlDeliveryPerson.setVisibility(View.GONE);

                                    } else if (model.getResult().getStatus().equals("PickedUp")) {
                                        binding.llButtons.setVisibility(View.VISIBLE);
                                        binding.btnAccept.setVisibility(View.VISIBLE);
                                        binding.tvAfaryCode.setVisibility(View.VISIBLE);
                                        // binding.btnAccept.setText(getString(R.string.track_order));
                                        binding.rlDeliveryPerson.setVisibility(View.GONE);

                                    } else if (model.getResult().getStatus().equals("Accepted")) {

                                        if (model.getResult().getSelfCollect().equals("Yes")) {
                                            binding.llButtons.setVisibility(View.VISIBLE);
                                            binding.btnAccept.setText(getString(R.string.delivery));
                                            binding.btnAccept.setVisibility(View.VISIBLE);
                                            binding.tvAfaryCode.setVisibility(View.GONE);
                                            binding.rlDeliveryPerson.setVisibility(View.GONE);
                                        } else {
                                            binding.llButtons.setVisibility(View.VISIBLE);
                                            binding.btnAccept.setVisibility(View.GONE);
                                            binding.tvAfaryCode.setVisibility(View.VISIBLE);
                                            binding.rlDeliveryPerson.setVisibility(View.VISIBLE);
                                        }

                                    } else if (model.getResult().getStatus().equals("Completed")) {
                                        binding.llButtons.setVisibility(View.GONE);
                                        binding.tvAfaryCode.setVisibility(View.VISIBLE);
                                        binding.rlDeliveryPerson.setVisibility(View.GONE);

                                    } else if (model.getResult().getStatus().equals("Cancelled")) {
                                        binding.llButtons.setVisibility(View.VISIBLE);
                                        binding.tvAfaryCode.setVisibility(View.GONE);

                                        binding.btnAccept.setVisibility(View.GONE);
                                        binding.btnChat.setVisibility(View.GONE);
                                        binding.btnDecline.setVisibility(View.VISIBLE);
                                        binding.btnDecline.setText(getString(R.string.cancelled));


                                    } else if (model.getResult().getStatus().equals("Cancelled_by_user")) {
                                        binding.llButtons.setVisibility(View.VISIBLE);
                                        binding.tvAfaryCode.setVisibility(View.GONE);

                                        binding.btnAccept.setVisibility(View.GONE);
                                        binding.btnChat.setVisibility(View.GONE);
                                        binding.btnDecline.setVisibility(View.VISIBLE);
                                        binding.btnDecline.setText(getString(R.string.cancelled));

                                    }
                                    arrayList.clear();
                                    arrayList.addAll(model.getResult().getProductList());
                                    itemsAdapter.notifyDataSetChanged();
                                    Log.e("delivery=====", model.getResult().getDeliveryPerson() + "");
                                    //    if(jsonObject.getJSONObject("result").getJSONObject("delivery_person")==null)  { //    model.getResult().getDeliveryPerson()==null){
                                    if (jsonObject.getJSONObject("result").isNull("delivery_person")) { //    model.getResult().getDeliveryPerson()==null){
                                        binding.rlDeliveryPerson.setVisibility(View.GONE);
                                    } else {
                                        //  if(jsonObject.getJSONObject("result").getJSONObject("delivery_person").has("")) {


                                        try {
                                            if (model.getResult().getDeliveryPerson() != null) {
                                                if (model.getResult().getDeliveryPerson().getStatus().equalsIgnoreCase("PickedUp"))
                                                    binding.rlDeliveryPerson.setVisibility(View.GONE);
                                                else
                                                    binding.rlDeliveryPerson.setVisibility(View.VISIBLE);
                                            }

                                            // if(model.getResult().getDeliveryPerson().getStatus().equalsIgnoreCase("PickedUp")) binding.rlDeliveryPerson.setVisibility(View.GONE);
                                            // else  binding.rlDeliveryPerson.setVisibility(View.VISIBLE);

                                            binding.tvDeliveryPerson.setText(Html.fromHtml("<font color='#000'>" + "<b>" + model.getResult().getDeliveryPerson().getDeliveryPersonName() + "</b>" + " the delivery person is on his way to you. please prepare the package so that he does not wait. Thanks" + "</font>"));
                                            //  binding.btnAccept.setText(getString(R.string.track_order));
                                            //   binding.btnAccept.setVisibility(View.GONE);
                                        } catch (Exception e) {

                                        }
                                    }

                                }


                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                // binding.tvNotFount.setVisibility(View.VISIBLE);
                                arrayList.clear();
                                itemsAdapter.notifyDataSetChanged();
                                Toast.makeText(OrderDetailsAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            } else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(OrderDetailsAct.this);
                            }
                        } else {
                            //  binding.tvNotFount.setVisibility(View.VISIBLE);
                            Toast.makeText(OrderDetailsAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }

            if (dynamicResponseModel.getJsonObject() != null) {
                pauseProgressDialog();
                if (dynamicResponseModel.getApiName() == ApiConstant.ACCEPT_DECLINE_ORDER) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                // binding.tvNotFount.setVisibility(View.GONE);
                                if (orderStatus.equals("Accepted")) {
                                    Toast.makeText(OrderDetailsAct.this, getString(R.string.order_accepted), Toast.LENGTH_SHORT).show();
                                    callOrderDetail();
                                    if (model.getResult().getSelfCollect().equals("No"))
                                        callAssignDelivery();


                                } else {
                                    if (type.equals("All")) {
                                        HashMap<String, String> map = new HashMap<>();
                                        map.put("common_order_id", orderId);
                                        Log.e("informDelivery order cancel====", map.toString());
                                        orderDetailsViewModel.informDeliveryOrderCancel(OrderDetailsAct.this, map);
                                    } else {
                                        Toast.makeText(OrderDetailsAct.this, getString(R.string.order_cancelled), Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                // binding.tvNotFount.setVisibility(View.VISIBLE);
                                Toast.makeText(OrderDetailsAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            } else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(OrderDetailsAct.this);
                            }
                        } else {
                            //  binding.tvNotFount.setVisibility(View.VISIBLE);
                            Toast.makeText(OrderDetailsAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }


            if (dynamicResponseModel.getJsonObject() != null) {
                pauseProgressDialog();
                if (dynamicResponseModel.getApiName() == ApiConstant.INFORM_DELIVERY_CANCEL_ORDER) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").equals("OK")) {
                                // binding.tvNotFount.setVisibility(View.GONE);
                                Toast.makeText(OrderDetailsAct.this, getString(R.string.order_cancelled), Toast.LENGTH_SHORT).show();
                                finish();
                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                // binding.tvNotFount.setVisibility(View.VISIBLE);
                                Toast.makeText(OrderDetailsAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            } else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(OrderDetailsAct.this);
                            }
                        } else {
                            //  binding.tvNotFount.setVisibility(View.VISIBLE);
                            Toast.makeText(OrderDetailsAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }


            if (dynamicResponseModel.getJsonObject() != null) {
                pauseProgressDialog();
                if (dynamicResponseModel.getApiName() == ApiConstant.GENERATE_TOKEN) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").equals("OK")) {
                                // binding.tvNotFount.setVisibility(View.GONE);
                                token = jsonObject.getString("access_token");
                                Log.e("Access Token===", token);
                            } else if (jsonObject.getString("status").toString().equals("NONOK")) {
                                // binding.tvNotFount.setVisibility(View.VISIBLE);
                                Toast.makeText(OrderDetailsAct.this, jsonObject.getString("msg_content").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            //  binding.tvNotFount.setVisibility(View.VISIBLE);
                            Toast.makeText(OrderDetailsAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }


            if (dynamicResponseModel.getJsonObject() != null) {
                pauseProgressDialog();
                if (dynamicResponseModel.getApiName() == ApiConstant.ADD_DELIVERY) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            //  callAcceptDeclineOrder(orderStatus,type,orderId,"");
                            Log.e("delivery api response====", jsonObject.toString());



                         /*   if (jsonObject.getString("status").equals("OK")) {
                             //   callAcceptDeclineOrder(orderStatus,type,orderId,"");
                            } else if (jsonObject.getString("status").equals("NONOK")) {
                                // binding.tvNotFount.setVisibility(View.VISIBLE);
                               // Toast.makeText(OrderDetailsAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();

                            }
                            else if (jsonObject.getString("status").equals("FAILED")) {
                                // binding.tvNotFount.setVisibility(View.VISIBLE);
                                Toast.makeText(OrderDetailsAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }*/


                        } else {
                            //  binding.tvNotFount.setVisibility(View.VISIBLE);
                            //  Toast.makeText(OrderDetailsAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("response===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            Log.e("delivery api response====", jsonObject.toString());

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        // callAcceptDeclineOrder(orderStatus,type,orderId,"");

                    }
                }


            }


            if (dynamicResponseModel.getJsonObject() != null) {
                pauseProgressDialog();
                if (dynamicResponseModel.getApiName() == ApiConstant.COMPLETE_SELF_COLLECT_ORDER) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                // binding.tvNotFount.setVisibility(View.GONE);
                                Toast.makeText(OrderDetailsAct.this, getString(R.string.order_completed), Toast.LENGTH_SHORT).show();
                                callOrderDetail();

                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                // binding.tvNotFount.setVisibility(View.VISIBLE);
                                Toast.makeText(OrderDetailsAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            } else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(OrderDetailsAct.this);
                            }
                        } else {
                            //  binding.tvNotFount.setVisibility(View.VISIBLE);
                            Toast.makeText(OrderDetailsAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }


            if (dynamicResponseModel.getJsonObject() != null) {
                pauseProgressDialog();
                if (dynamicResponseModel.getApiName() == ApiConstant.RETURN_SELLER_PRODUCT) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            Log.e("response return product===", stringResponse);

                            if (jsonObject.getString("status").toString().equals("1")) {
                                // binding.tvNotFount.setVisibility(View.GONE);
                                Toast.makeText(OrderDetailsAct.this, getString(R.string.product_received), Toast.LENGTH_SHORT).show();
                                callOrderDetail();


                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                // binding.tvNotFount.setVisibility(View.VISIBLE);
                                Toast.makeText(OrderDetailsAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            //  binding.tvNotFount.setVisibility(View.VISIBLE);
                            Toast.makeText(OrderDetailsAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }


            if (dynamicResponseModel.getJsonObject() != null) {
                pauseProgressDialog();
                if (dynamicResponseModel.getApiName() == ApiConstant.INTERNATIONAL_TRANSIT_DELIVERY) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            Log.e("response international order ===", stringResponse);

                            if (jsonObject.getString("status").toString().equals("1")) {
                                // binding.tvNotFount.setVisibility(View.GONE);
                                callOrderDetail();


                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                // binding.tvNotFount.setVisibility(View.VISIBLE);
                                Toast.makeText(OrderDetailsAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            //  binding.tvNotFount.setVisibility(View.VISIBLE);
                            Toast.makeText(OrderDetailsAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }


        });
    }

    private void observeLoader() {
        orderDetailsViewModel.isLoading.observe(OrderDetailsAct.this, aBoolean -> {
            if (aBoolean) {
                showProgressDialog(OrderDetailsAct.this, false, getString(R.string.please_wait));
            } else {
                pauseProgressDialog();
            }
        });
    }

    @Override
    public void orderItem(int position, OrderDetailsModel.Result.Product product, String tag) {
        // alertCancelOrder("Are you sure you want to refuse this item?","Cancelled","Single",product.getOrderId());
        if (tag.equals("Cancel")) {
            orderStatus = "Cancelled";
            type = "Single";
           // dialogCancelOrderReason("Cancelled", type, product.getOrderId());


                dialogSelectCancelOrderReason(orderStatus, type, product.getOrderId());



        } else {
            alertOrderReturn(product.getOrderId());
        }

    }

    private void alertOrderReturn(String orderId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailsAct.this);
        builder.setMessage(getString(R.string.are_you_sure_delivery_man_has_been_returned_product_to_you))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        returnOrder(orderId);
                    }
                }).setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }


    private void dialogSelectCancelOrderReason(String orderStatus, String type, String id) {

        Dialog mDialog = new Dialog(OrderDetailsAct.this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_select_cancel_reason);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);

        AppCompatButton btnCancel = mDialog.findViewById(R.id.btnCancel);
        AppCompatButton btnSubmit = mDialog.findViewById(R.id.btnSubmit);

        CheckBox checkOutOfStock = mDialog.findViewById(R.id.checkOutOfStock);
        CheckBox checkAnother = mDialog.findViewById(R.id.checkAnother);


        btnCancel.setOnClickListener(v -> {
            mDialog.dismiss();

        });


        checkOutOfStock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // If the first CheckBox is checked, uncheck the second
                if (isChecked) {
                    checkOutOfStock.setChecked(true);  // Uncheck the other checkbox
                    checkAnother.setChecked(false);  // Uncheck the other checkbox
                    reason = "";   //getString(R.string.product_is_out_of_stock);
                    selectCheck ="out of stock";
                }
            }
        });

        checkAnother.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // If the second CheckBox is checked, uncheck the first
                if (isChecked) {
                    checkOutOfStock.setChecked(false);  // Uncheck the other checkbox
                    checkAnother.setChecked(true);  // Uncheck the other checkbox
                    reason = "enter reason";
                    selectCheck ="in stock";
                }
            }
        });


        btnSubmit.setOnClickListener(v -> {
          //  mDialog.dismiss();
            // callCompleteSelfCollect(edReason.getText().toString());
            //  callAcceptDeclineOrder(orderStatus,type,orderId,edReason.getText().toString());

            if(selectCheck.equals(""))     {
                Toast.makeText(OrderDetailsAct.this, getString(R.string.please_select_atleast_one), Toast.LENGTH_LONG).show();

            }
            else if(selectCheck.equals("in stock")){
                if (reason.equals("")) {
                    Toast.makeText(OrderDetailsAct.this, getString(R.string.select_reason), Toast.LENGTH_LONG).show();
                } else {
                    mDialog.dismiss();
                   // dialogCancelOrderReason(orderStatus, "All", id,userId);
                    dialogCancelOrderReason(orderStatus, "All", orderId);

                }
            }
            else {
                mDialog.dismiss();
               // callAcceptDeclineOrder(orderStatus, type, id, userId,reason);
                callAcceptDeclineOrder(orderStatus, type, id, reason);

            }

/*
            if (reason.equals("")) {
                Toast.makeText(OrderDetailsAct.this, getString(R.string.select_reason), Toast.LENGTH_LONG).show();
            } else if (!reason.equals("enter reason")) {
                mDialog.dismiss();
                callAcceptDeclineOrder(orderStatus, type, id, reason);
            } else {
                mDialog.dismiss();
                dialogCancelOrderReason(orderStatus, "All", orderId);
            }
*/


        });
        mDialog.show();
    }


    private void dialogCancelOrderReason(String orderStatus, String type, String id) {

        Dialog mDialog = new Dialog(OrderDetailsAct.this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_cancel_reason);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);

        EditText edReason = mDialog.findViewById(R.id.edReason);
        AppCompatButton btnCancel = mDialog.findViewById(R.id.btnCancel);
        AppCompatButton btnSubmit = mDialog.findViewById(R.id.btnSubmit);

        btnCancel.setOnClickListener(v -> {
            mDialog.dismiss();

        });

        btnSubmit.setOnClickListener(v -> {
            mDialog.dismiss();
            // callCompleteSelfCollect(edReason.getText().toString());
            //  callAcceptDeclineOrder(orderStatus,type,orderId,edReason.getText().toString());
            callAcceptDeclineOrder(orderStatus, type, id, edReason.getText().toString());


        });
        mDialog.show();
    }


    private int parseFrenchNumber(String number) {
        // Remove the commas and parse to an integer
        String cleanedNumber = number.replace(",", "");
        return Integer.parseInt(cleanedNumber);
    }

    // Function to format number in French style (with commas)
    private String formatToFrenchStyle(int number) {
        // Use the NumberFormat for the French locale
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.FRANCE);
        return numberFormat.format(number);
    }

    //


    private void returnOrder(String orderId) {
        HashMap<String, String> map = new HashMap<>();
        map.put("order_id", orderId);
        orderDetailsViewModel.returnOrder(OrderDetailsAct.this, map);
    }


}
