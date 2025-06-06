package com.afaryseller.ui.bookedorder;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;

import com.afaryseller.R;
import com.afaryseller.core.BaseFragment;
import com.afaryseller.databinding.OrderFragmentBinding;
import com.afaryseller.retrofit.ApiConstant;
import com.afaryseller.retrofit.Constant;
import com.afaryseller.ui.notifications.NotificationAct;
import com.afaryseller.ui.orderdetails.OrderDetailsAct;
import com.afaryseller.utility.DataManager;
import com.afaryseller.utility.SessionManager;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderFragment extends BaseFragment<OrderFragmentBinding, OrderViewModel> implements OrderListener {
    OrderFragmentBinding binding;
    OrderViewModel orderViewModel;
    String catId = "",reason = "",selectCheck="";
    OrderAdapter adapter;
    ArrayList<OrderModel.Result> arrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.order_fragment, container, false);
        orderViewModel = new OrderViewModel();
        binding.setOrderViewModel(orderViewModel);
        binding.getLifecycleOwner();
        orderViewModel.init(getActivity());
        initViews();
        return binding.getRoot();
    }

    private void initViews() {

        if (this.getArguments() != null) {
            catId = getArguments().getString("id");

        }
        arrayList = new ArrayList<>();

        adapter = new OrderAdapter(getActivity(), arrayList, OrderFragment.this);
        binding.rvOrders.setAdapter(adapter);


        observeLoader();
        observeResponse();

        binding.RRback.setOnClickListener(v -> getActivity().onBackPressed());
    }

    private void callGetAllOrder() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + DataManager.getInstance().getUserData(getActivity()).getResult().getAccessToken());
        headerMap.put("Accept", "application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());
        map.put("cat_id", catId);
        map.put("seller_register_id", DataManager.getInstance().getUserData(getActivity()).getResult().getRegisterId());
        map.put("user_seller_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());

        map.put("sub_seller_id", DataManager.getInstance().getUserData(requireActivity()).getResult().getSub_seller_id());
        map.put("shop_id", SessionManager.readString(requireActivity(), Constant.shopId, ""));

        orderViewModel.getAllOrder(getActivity(), headerMap, map);
    }

    private void callAcceptDeclineOrder(String status, String type, String orderId, String userId, String reason) {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + DataManager.getInstance().getUserData(getActivity()).getResult().getAccessToken());
        headerMap.put("Accept", "application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("seller_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());
        map.put("user_id", userId);
        map.put("order_id", orderId);
        map.put("status", status);
        map.put("type", type);
        map.put("reason", reason);
        map.put("seller_register_id", DataManager.getInstance().getUserData(getActivity()).getResult().getRegisterId());
        map.put("user_seller_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());

        //  map.put("sub_seller_id", DataManager.getInstance().getUserData(requireActivity()).getResult().getSub_seller_id());
        //  map.put("shop_id",SessionManager.readString(requireActivity(), Constant.shopId, ""));

        Log.e("orderAcceptDecline====", map.toString());

        orderViewModel.acceptDeclineOrder(getActivity(), headerMap, map);
    }


    public void observeResponse() {
        orderViewModel.isResponse.observe(getActivity(), dynamicResponseModel -> {
            if (dynamicResponseModel.getJsonObject() != null) {
                pauseProgressDialog();
                if (dynamicResponseModel.getApiName() == ApiConstant.GET_ALL_ORDER) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                // binding.tvNotFount.setVisibility(View.GONE);
                                OrderModel model = new Gson().fromJson(stringResponse, OrderModel.class);
                                binding.tvNotFound.setVisibility(View.GONE);
                                arrayList.clear();
                                arrayList.addAll(model.getResult());
                                adapter.notifyDataSetChanged();
                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                // binding.tvNotFount.setVisibility(View.VISIBLE);
                                binding.tvNotFound.setVisibility(View.VISIBLE);
                                arrayList.clear();
                                adapter.notifyDataSetChanged();
                                // Toast.makeText(getActivity(), jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            } else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(getActivity());
                            }
                        } else {
                            //  binding.tvNotFount.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity(), dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                if (dynamicResponseModel.getApiName() == ApiConstant.ACCEPT_DECLINE_ORDER) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                // binding.tvNotFount.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), getString(R.string.order_cancelled), Toast.LENGTH_SHORT).show();
                                callGetAllOrder();

                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                // binding.tvNotFount.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(), jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            } else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(getActivity());
                            }

                        } else {
                            //  binding.tvNotFount.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity(), dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    private void observeLoader() {
        orderViewModel.isLoading.observe(getActivity(), aBoolean -> {
            if (aBoolean) {
                showProgressDialog(getActivity(), false, getString(R.string.please_wait));
            } else {
                pauseProgressDialog();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        callGetAllOrder();

    }

    @Override
    public void onOrder(OrderModel.Result result) {
        //  alertCancelOrder("Cancelled",result.getOrderId(),result.getUserId());
       // dialogCancelOrderReason("Cancelled", "All", result.getOrderId(), result.getUserId());

        dialogSelectCancelOrderReason("Cancelled", "All", result.getOrderId(), result.getUserId());

    }
    private void dialogSelectCancelOrderReason(String orderStatus, String type, String id,String userId) {

        Dialog mDialog = new Dialog(requireActivity());
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
                    reason = "";//getString(R.string.product_is_out_of_stock);
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
                Toast.makeText(requireActivity(), getString(R.string.please_select_atleast_one), Toast.LENGTH_LONG).show();

            }
            else if(selectCheck.equals("in stock")){
               if (reason.equals("")) {
                   Toast.makeText(requireActivity(), getString(R.string.select_reason), Toast.LENGTH_LONG).show();
               } else {
                   mDialog.dismiss();
                   dialogCancelOrderReason(orderStatus, "All", id,userId);
               }
           }
           else {
               mDialog.dismiss();
               callAcceptDeclineOrder(orderStatus, type, id, userId,reason);
           }


/*
            if (reason.equals("")) {
                Toast.makeText(requireActivity(), getString(R.string.select_reason), Toast.LENGTH_LONG).show();
            } else if (!reason.equals("enter reason")) {
                mDialog.dismiss();
                callAcceptDeclineOrder(orderStatus, type, id, userId,reason);
            } else {
                mDialog.dismiss();
                dialogCancelOrderReason(orderStatus, "All", id,userId);
            }
*/


        });
        mDialog.show();
    }


    private void dialogCancelOrderReason(String orderStatus, String type, String orderId, String userId) {

        Dialog mDialog = new Dialog(requireActivity());
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
            callAcceptDeclineOrder(orderStatus, type, orderId, userId, edReason.getText().toString());


        });
        mDialog.show();
    }


    private void alertCancelOrder(String orderStatus, String orderId, String userId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to refuse this order?")
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        //   callAcceptDeclineOrder(orderStatus,orderId,userId);
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


}