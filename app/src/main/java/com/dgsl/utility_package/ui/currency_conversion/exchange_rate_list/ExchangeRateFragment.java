package com.dgsl.utility_package.ui.currency_conversion.exchange_rate_list;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import utility_package.R;
import com.dgsl.utility_package.data.api.ApiClient;
import com.dgsl.utility_package.data.api.ApiInterface;
import com.dgsl.utility_package.data.model.ConversionRateListModel;
import com.dgsl.utility_package.data.roomModal.CurrencyDataModel;
import com.dgsl.utility_package.ui.currency_conversion.CurrencyConvertViewModel;
import com.dgsl.utility_package.ui.currency_conversion.currency_convert.AutoCompleteAdapter;
import com.dgsl.utility_package.util.ConverterUtil;
import com.dgsl.utility_package.util.CountryFlagUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExchangeRateFragment extends Fragment {

    RecyclerView listView;
    public List<ConversionRateListModel> listModel = new ArrayList<>();
    ProgressBar progressView;
    AutoCompleteTextView exchangeRateDropDownList;
    CurrencyConvertViewModel viewModel;
    AutoCompleteAdapter adapter;
    View exchangeFlagView;
    TextView exchangeFlagTV;
    Call<JsonObject> CurrencyDataCall;
    public static boolean oneTimeDisplayToggle = true;
    List<CurrencyDataModel> allCurrencyOfflineDataList = new ArrayList<>();
    public ExchangeRateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exchange_rate, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hideKeyboard(getView().getContext());
        listView = getView().findViewById(R.id.exchange_RV);
        exchangeFlagTV = getView().findViewById(R.id.flagExchangeRateTV);
        exchangeFlagView = getView().findViewById(R.id.flagExchangeRateView);
        exchangeFlagView.setVisibility(View.GONE);
        progressView = getView().findViewById(R.id.progressView);
        exchangeRateDropDownList = getView().findViewById(R.id.filled_exposed_dropdown_exchange_rate);

        viewModel  = new ViewModelProvider(this).get(CurrencyConvertViewModel.class);
//        viewModel.getAllCurrencyData().observe(getViewLifecycleOwner(), new Observer<List<CurrencyDataModel>>() {
//            @Override
//            public void onChanged(List<CurrencyDataModel> models) {
//                // when the data is changed in our models we are
//                // adding that list to our adapter class.
//                allCurrencyOfflineDataList.clear();
//                allCurrencyOfflineDataList = models;
//                getCurrencyData("INR",models);
//                putDataInDropDownMenu((ArrayList<ConversionRateListModel>) listModel);
//            }
//        });
        getCurrencyDataAPICAll("INR");

        exchangeRateDropDownList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                getCurrencyData(exchangeRateDropDownList.getText().toString(),allCurrencyOfflineDataList);
                getCurrencyDataAPICAll(exchangeRateDropDownList.getText().toString());
            }
        });
    }
    private void putDataInDropDownMenu(ArrayList<ConversionRateListModel> models){
        try{
            adapter = new AutoCompleteAdapter(getContext(),
                    R.layout.drop_down_menu_item,models);

            exchangeRateDropDownList.setAdapter(adapter);

            adapter.notifyDataSetChanged();

            if(oneTimeDisplayToggle){
                ConversionRateListModel conversionRateListModel = (ConversionRateListModel) exchangeRateDropDownList.getAdapter().getItem(61);
                exchangeRateDropDownList.setText(conversionRateListModel.currency_code, false);
                oneTimeDisplayToggle = false;
            }
        }catch(Exception e){

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getCurrencyData(String code , List<CurrencyDataModel> models) {
      try{
          listModel.clear();
          String json = ConverterUtil.findCurrencyDataHelper(code,models);
          if(!json.isEmpty() && json != null){
              JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
              listModel = ConverterUtil.convertJsonObject(jsonObject,true);
              putDataInDropDownMenu((ArrayList<ConversionRateListModel>) listModel);
              PutDataIntoRecyclerView(getView().getContext(),listModel);
              setFlagInTextView();
          }else{
            getCurrencyDataAPICAll(code);
          }

      }catch(Exception e){
          System.out.println("error found :"+ e.getLocalizedMessage());
          }
    }
    private void hideKeyboard(Context context){
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    private void PutDataIntoRecyclerView(Context context, List<ConversionRateListModel> currencyData){
        try{
            ExchangeRateListAdapter adapter = new ExchangeRateListAdapter(context,currencyData);
            listView.setLayoutManager(new LinearLayoutManager(context));
            listView.addItemDecoration(new DividerItemDecoration(context,
                    DividerItemDecoration.VERTICAL));
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }catch(Exception e){

        }
    }

    private void setFlagInTextView(){

        if(!exchangeRateDropDownList.getText().toString().isEmpty() && !(exchangeRateDropDownList.getText().toString() == null)){
            exchangeFlagView.setVisibility(View.VISIBLE);
            String flag = exchangeRateDropDownList.getText().toString().substring(0,2);
            exchangeFlagTV.setText(CountryFlagUtil.getCountryFlagByCountryCode(flag));
        }else{
            exchangeFlagView.setVisibility(View.GONE);
        }
    }

    private void getCurrencyDataAPICAll(String name){
        try{
            progressView.setVisibility(View.VISIBLE);
            ApiInterface apiInterface;
            apiInterface = ApiClient.getClient().create(ApiInterface.class);
            ApiClient apiClient = new ApiClient();
            CurrencyDataCall = apiInterface.getCurrencyConversion(apiClient.apiKey, name);

            CurrencyDataCall.enqueue(new Callback<JsonObject>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    JsonObject obj = response.body().getAsJsonObject("data");
                    try {
                        listModel.clear();
                        listModel = ConverterUtil.convertJsonObject(obj,true);
                        if(getView().getContext() != null){
                            PutDataIntoRecyclerView(getView().getContext(),listModel);
                        }
                        putDataInDropDownMenu((ArrayList<ConversionRateListModel>) listModel);
                        progressView.setVisibility(View.GONE);
                        setFlagInTextView();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

//                    Log.e("TAG", "Success: " + response.body());
                }
                @Override
                public void onFailure(Call <JsonObject> callback, Throwable t) {
                    Log.e("TAG", "onFailure: " + t.getLocalizedMessage() );
                    progressView.setVisibility(View.GONE);
                }
            });
        }catch (Exception e){
            Log.e("ERROR_here", "onFailure: " + e.getLocalizedMessage() );
            progressView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(CurrencyDataCall!= null){
            CurrencyDataCall.cancel();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(CurrencyDataCall!= null){
            CurrencyDataCall.cancel();
        }
    }
}