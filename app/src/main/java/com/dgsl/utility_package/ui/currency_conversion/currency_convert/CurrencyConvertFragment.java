package com.dgsl.utility_package.ui.currency_conversion.currency_convert;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.dgsl.utility_package.data.api.ApiClient;
import com.dgsl.utility_package.data.api.ApiInterface;
import com.dgsl.utility_package.data.model.ConversionRateListModel;
import com.dgsl.utility_package.data.roomModal.CurrencyDataModel;
import com.dgsl.utility_package.ui.currency_conversion.CurrencyConvertViewModel;
import com.dgsl.utility_package.util.ConverterUtil;
import com.dgsl.utility_package.util.CountryFlagUtil;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utility_package.R;

public class CurrencyConvertFragment extends Fragment {
    public ArrayList<ConversionRateListModel> listModel = new ArrayList<>();

    String baseCode,toConvertCode;
    double baseCurrencyCode, toCurrencyCode;
    View progressView;
    EditText inputValue,resultField;
    AutoCompleteTextView editTextFilledExposedDropDownToConvert;
    AutoCompleteTextView editTextFilledExposedDropdownFromConvert;
    AutoCompleteAdapter adapter;
    Button convertBtn;
    boolean swapToggle = false;
    public static boolean toggle = false;
    public static boolean oneTimeDisplayToggle = true;
    CurrencyConvertViewModel viewModel;
    ConverterUtil converterUtil;
    TextView firstFlagTV, secondFlagTV;
    View swapView, flagFirstView, flagSecondView;
    List<CurrencyDataModel> allCurrencyData = new ArrayList<>();
    Call<JsonObject> CurrencyDataCall;
    CountryFlagUtil countryFlagUtil = new CountryFlagUtil();

    public CurrencyConvertFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        getData();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hideKeyboard(requireView().getContext());
        converterUtil = new ConverterUtil(requireView().getContext());
        firstFlagTV = requireView().findViewById(R.id.flagFirstTV);
        secondFlagTV = requireView().findViewById(R.id.flagSecondTV);
        flagFirstView = requireView().findViewById(R.id.firstFlagView);
        flagSecondView = requireView().findViewById(R.id.secondFlagView);
        progressView = requireView().findViewById(R.id.progressView);
        View refreshBtn = requireView().findViewById(R.id.refreshBtn);
        TextView updateTimeDate_TV = requireView().findViewById(R.id.updateTimeDate_TV);
        inputValue = requireView().findViewById(R.id.inputValue_TV);
        convertBtn = requireView().findViewById(R.id.convert_Btn);
        resultField = requireView().findViewById(R.id.result_EditText);
        resultField.setFocusable(false);
        resultField.setEnabled(false);
        swapView = requireView().findViewById(R.id.swap_ImageView);

        viewModel  = new ViewModelProvider(this).get(CurrencyConvertViewModel.class);

        //first list to choose from value to convert

        editTextFilledExposedDropDownToConvert =
                requireView().findViewById((R.id.filled_exposed_dropdown_convert_menu));


        //second list to convert to value
        editTextFilledExposedDropdownFromConvert =
                requireView().findViewById((R.id.filled_exposed_dropdown_main));

        editTextFilledExposedDropDownToConvert.setOnItemClickListener((parent, view1, position, id) -> {
            setFlagInTextView("second");
        });

        getData();

        editTextFilledExposedDropdownFromConvert.setOnItemClickListener((parent, view1, position, id) -> {
                if(converterUtil.isNetworkAvailable()){
                    getCurrencyDataAPICAll(editTextFilledExposedDropdownFromConvert.getText().toString());
                }else{
                    getCurrencyOfflineData(editTextFilledExposedDropdownFromConvert.getText().toString(),allCurrencyData);
                }

            });

        //set default value selected INR

        //get current time from device
        Date currentTime = Calendar.getInstance().getTime();
        updateTimeDate_TV.setText(""+currentTime);

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date currentTime = Calendar.getInstance().getTime();
                updateTimeDate_TV.setText(""+currentTime);
//                Toast.makeText(getActivity().getApplicationContext(),"hello clicked refresh",Toast.LENGTH_SHORT).show();
                getData();
            }
        });


        convertBtn.setOnClickListener(v -> {
            convertBtnFunc();
        });

        swapView.setOnClickListener(v -> {
            swapToggle = true;
            String editText1 = editTextFilledExposedDropdownFromConvert.getText().toString();
            String editText2 = editTextFilledExposedDropDownToConvert.getText().toString();
            if(!editText1.isEmpty() && !editText2.isEmpty() && !inputValue.getText().toString().isEmpty()){
                editTextFilledExposedDropdownFromConvert.setText(editText2,false);
                editTextFilledExposedDropDownToConvert.setText(editText1,false);
                setFlagInTextView("second");
                getCurrencyDataAPICAll(editText2);
            }else{
                Toast.makeText(getContext(), "Please enter required fields", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void convertBtnFunc(){
        hideKeyboard(requireView().getContext());
        baseCode = editTextFilledExposedDropdownFromConvert.getText().toString();
        baseCurrencyCode = ConverterUtil.baseValue(baseCode,listModel);
        toConvertCode = editTextFilledExposedDropDownToConvert.getText().toString();
        toCurrencyCode = ConverterUtil.baseValue(toConvertCode,listModel);


        if(!toConvertCode.isEmpty() && !baseCode.isEmpty() && !inputValue.getText().toString().isEmpty()){
            double resultValue = ConverterUtil.convertValue(Double.parseDouble(inputValue.getText().toString()),toCurrencyCode);
            resultField.setText(""+resultValue);
        }else{
            Toast.makeText(requireView().getContext(), "Please enter required field", Toast.LENGTH_SHORT).show();
        }
    }


    private void getData(){

        if(converterUtil.isNetworkAvailable()){
            if(!toggle){
                getCurrencyDataAPICAll("INR");
                toggle = true;
            }else if(allCurrencyData.isEmpty() && editTextFilledExposedDropDownToConvert.getText().toString().isEmpty()){
                getCurrencyDataAPICAll("INR");
            }
        }else{
            viewModel.getAllCurrencyData().observe(getViewLifecycleOwner(), new Observer<List<CurrencyDataModel>>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onChanged(List<CurrencyDataModel> models) {
                    // when the data is changed in our models we are
                    // adding that list to our adapter class.
                    allCurrencyData.clear();
                    allCurrencyData = models;
                    getCurrencyOfflineData("INR",allCurrencyData);
                }
            });
        }
    }

    private void hideKeyboard(Context context){
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_currency_convert, container, false);
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
                        listModel = ConverterUtil.convertJsonObject(obj,false);
                        putDataInDropDownMenu(listModel);
                        viewModel.insert(new CurrencyDataModel(name,obj.toString()));
                        progressView.setVisibility(View.GONE);

                        if(swapToggle){
                            convertBtnFunc();
                            swapToggle = false;
                        }
                        setFlagInTextView("first");

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

    private void setFlagInTextView(String flagSide){
        String flag;
        switch(flagSide){
            case "first":
                flagFirstView.setVisibility(View.VISIBLE);
                if(!editTextFilledExposedDropdownFromConvert.getText().toString().isEmpty() && !(editTextFilledExposedDropdownFromConvert.getText().toString()==null)){
                    flag = editTextFilledExposedDropdownFromConvert.getText().toString().substring(0,2);
                    firstFlagTV.setText(CountryFlagUtil.getCountryFlagByCountryCode(flag));
                }

                break;
            case "second":
                flagSecondView.setVisibility(View.VISIBLE);
                if(!editTextFilledExposedDropDownToConvert.getText().toString().isEmpty() && !(editTextFilledExposedDropDownToConvert.getText().toString()==null)){
                    flag = editTextFilledExposedDropDownToConvert.getText().toString().substring(0,2);
                    secondFlagTV.setText(CountryFlagUtil.getCountryFlagByCountryCode(flag));
                }

                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getCurrencyOfflineData(String code, List<CurrencyDataModel> models) {
        try{
            listModel.clear();
            String json = ConverterUtil.findCurrencyDataHelper(code,models);
            if(!json.isEmpty() && !models.isEmpty()){
                JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
                listModel = ConverterUtil.convertJsonObject(jsonObject,false);
                putDataInDropDownMenu(listModel);
            }else{
                Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                        "no offline data available", Snackbar.LENGTH_LONG);
                snackBar.show();
//              getCurrencyDataAPICAll("INR");
            }

        }catch(Exception e){

        }
    }

    private void putDataInDropDownMenu(ArrayList<ConversionRateListModel> models){
        try{
            if(!models.isEmpty()){
                adapter = new AutoCompleteAdapter(getContext(),
                        R.layout.drop_down_menu_item,models);
                editTextFilledExposedDropdownFromConvert.setAdapter(adapter);
                editTextFilledExposedDropDownToConvert.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                if(oneTimeDisplayToggle){
                    ConversionRateListModel conversionRateListModel = (ConversionRateListModel) editTextFilledExposedDropDownToConvert.getAdapter().getItem(61);
                    editTextFilledExposedDropdownFromConvert.setText(conversionRateListModel.currency_code, false);
                    oneTimeDisplayToggle = false;
                }else if(editTextFilledExposedDropdownFromConvert.getText().toString().isEmpty()){
                    ConversionRateListModel conversionRateListModel = (ConversionRateListModel) editTextFilledExposedDropDownToConvert.getAdapter().getItem(61);
                    editTextFilledExposedDropdownFromConvert.setText(conversionRateListModel.currency_code, false);
                }
            }
        }catch (NullPointerException e){

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