package com.example.roadexp_transporter.Review;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roadexp_transporter.R;

import org.w3c.dom.Text;

public class ReviewBottomSheet extends BottomSheetDialogFragment {

    private View mRoot;

    private String mInterfaceRewiew = "";
    private String mperformaceRewview = "";
    private String mColorReview = "";

    public ReviewBottomSheet() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.bottom_sheet_review, container, false);

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
                mperformaceRewview = "Good";
                moveToNextPage(3);
            }
        });


        mRoot.findViewById(R.id.ok2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mperformaceRewview = "Ok";
                moveToNextPage(3);
            }
        });


        mRoot.findViewById(R.id.bad2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mperformaceRewview = "Bad";
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
        Toast.makeText(getActivity(),mInterfaceRewiew + " "+mperformaceRewview+" "+mColorReview,Toast.LENGTH_SHORT).show();
    }

    private void moveToNextPage(int page)
    {
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
