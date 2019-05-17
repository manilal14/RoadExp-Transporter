package com.example.roadexp_transporter.Review;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.roadexp_transporter.CommonForAll.MySingleton;
import com.example.roadexp_transporter.DriverPackage.DriverHomepage;
import com.example.roadexp_transporter.LoginSingUp.LoginSessionManager;
import com.example.roadexp_transporter.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.BASE_URL;
import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.NO_OF_RETRY;
import static com.example.roadexp_transporter.CommonForAll.CommanVariablesAndFunctuions.RETRY_SECONDS;
import static com.example.roadexp_transporter.LoginSingUp.LoginSessionManager.TRANS_ID;

public class ReviewBottomSheet extends BottomSheetDialogFragment {

    private View mRoot;

    private String mInterfaceRewiew = "";
    private String mperformaceReview = "";
    private String mColorReview = "";
    private ProgressBar mProgressBar;

    private String TAG = this.getClass().getSimpleName();

    public ReviewBottomSheet() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.bottom_sheet_review, container, false);

        mProgressBar = mRoot.findViewById(R.id.progress_bar);
        clickListener();
        return mRoot;
    }

    private void clickListener() {

        mRoot.findViewById(R.id.happy1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mInterfaceRewiew = "Good";
               moveToNextPage(2);
            }
        });
        mRoot.findViewById(R.id.ok1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterfaceRewiew = "Ok";
                moveToNextPage(2);
            }
        });
        mRoot.findViewById(R.id.bad1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterfaceRewiew = "Bad";
                moveToNextPage(2);
            }
        });

        mRoot.findViewById(R.id.happy2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mperformaceReview = "Good";
                moveToNextPage(3);
            }
        });


        mRoot.findViewById(R.id.ok2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mperformaceReview = "Ok";
                moveToNextPage(3);
            }
        });


        mRoot.findViewById(R.id.bad2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mperformaceReview = "Bad";
                moveToNextPage(3);
            }
        });




        mRoot.findViewById(R.id.happy3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mColorReview = "Good";
                moveToNextPage(4);
            }
        });


        mRoot.findViewById(R.id.ok3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mColorReview = "Ok";
                moveToNextPage(4);
            }
        });


        mRoot.findViewById(R.id.bad3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mColorReview = "Bad";
                moveToNextPage(4);
            }
        });

        mRoot.findViewById(R.id.review_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendReviewToDb();
                dismiss();

            }
        });


    }

    private void sendReviewToDb() {
        Toast.makeText(getActivity(),mInterfaceRewiew + " "+mperformaceReview+" "+mColorReview,Toast.LENGTH_SHORT).show();

        Log.e(TAG, "called : sendReviewToDb");
        String URL = BASE_URL + "appreview";

        mProgressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);
                Toast.makeText(getActivity(),"Response Success",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressBar.setVisibility(View.GONE);
                Log.e(TAG, error.toString());
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params =  new HashMap<>();
                LoginSessionManager session = new LoginSessionManager(getActivity());
                String transId = session.getTransporterDetailsFromPref().get(TRANS_ID);

                params.put("trans",transId);

                params.put("inter",mInterfaceRewiew);
                params.put("perfo",mperformaceReview);
                params.put("color_sc",mColorReview);

                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(RETRY_SECONDS, NO_OF_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }

    private void moveToNextPage(int page){
        LinearLayout interfaceReviewPage   = mRoot.findViewById(R.id.review_page1);
        LinearLayout performanceReviewPage = mRoot.findViewById(R.id.review_page2);
        LinearLayout colorReviewPage       = mRoot.findViewById(R.id.review_page3);
        LinearLayout finalPage             = mRoot.findViewById(R.id.review_page4);

        TextView title = mRoot.findViewById(R.id.review_title);

        Animation enterFromRight = AnimationUtils.loadAnimation(getActivity(), R.anim.enter_from_right);
        Animation exitToLeft     = AnimationUtils.loadAnimation(getActivity(), R.anim.exit_to_left);

        switch (page)
        {
            case 2:
                interfaceReviewPage.startAnimation(exitToLeft);
                interfaceReviewPage.setVisibility(View.GONE);

                title.setText("How do you feel about Performance of App?");

                performanceReviewPage.setVisibility(View.VISIBLE);
                performanceReviewPage.startAnimation(enterFromRight);


                break;

            case 3:
                performanceReviewPage.startAnimation(exitToLeft);
                performanceReviewPage.setVisibility(View.GONE);

                title.setText("How do you feel about color scheme ?");

                colorReviewPage.setVisibility(View.VISIBLE);
                colorReviewPage.startAnimation(enterFromRight);
                break;

            case 4:
                colorReviewPage.startAnimation(exitToLeft);
                colorReviewPage.setVisibility(View.GONE);

                title.setText("Thankyou\nfor your valuable feedback");
                finalPage.setVisibility(View.VISIBLE);

                finalPage.startAnimation(enterFromRight);
                break;

        }




    }
}
