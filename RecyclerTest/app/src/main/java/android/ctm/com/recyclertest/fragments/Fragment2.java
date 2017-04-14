package android.ctm.com.recyclertest.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.ctm.com.recyclertest.App;
import android.ctm.com.recyclertest.R;
import android.ctm.com.recyclertest.activities.MainActivity;
import android.ctm.com.recyclertest.adapters.RecyclerAdapter;
import android.ctm.com.recyclertest.models.NewsArticle;
import android.ctm.com.recyclertest.network.Urls;
import android.ctm.com.recyclertest.persistence.DBHelper;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment2.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment2 extends BaseFragment {

    private static final String LOG_TAG = Fragment2.class.getName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private List<NewsArticle> mArticles;
    private RecyclerAdapter mAdapter;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;

    private DBHelper db;
    // Progress dialog
    private ProgressDialog pDialog;

    public Fragment2() {
        // Required empty public constructor
    }

    private static final String PARAM_ARTICLES = "articles";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            //probably orientation change
            mArticles = (List<NewsArticle>) savedInstanceState.getSerializable(PARAM_ARTICLES);
            if(mArticles == null) {
                mArticles = new ArrayList<NewsArticle>();
            }
        } else {
            if (mArticles != null) {
                //returning from backstack, data is fine, do nothing
                return;
            } else {
                //newly created
                mArticles = new ArrayList<NewsArticle>();
            }
        }

        mAdapter = new RecyclerAdapter(mArticles);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        if(mArticles.isEmpty()) {
            makeNewsApiRequest();
            //makePOSTRequest();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the fragment's state here
        outState.putSerializable(PARAM_ARTICLES, (Serializable) mArticles);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment2.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment2 newInstance(String param1, String param2) {
        Fragment2 fragment = new Fragment2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void saveToDb(NewsArticle article){
        db.addContact(article);

        // Reading all contacts
        //Log.d("Reading: ", "Reading all contacts..");
        List<NewsArticle> contacts = db.getAllContacts();

        for (NewsArticle cn : contacts) {
            String log = "Id: " + cn.getId() + " ,Name: " + cn.getTitle() + " ,Phone: " + cn.getDescription();
            // Writing Contacts to log
            //Log.d("Name: ", log);
        }
    }

    private void makePOSTRequest() {

        String url = "http://httpbin.org/post";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("name", "Alif");
                params.put("domain", "http://itsalif.info");

                return params;
            }
        };

        App.getInstance().addToRequestQueue(postRequest);
    }

    private void makeNewsApiRequest() {
        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET,
                Urls.buildNewsUrl(), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //Log.e(TAG, response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object

                    JSONArray articles = response.getJSONArray("articles");
                    for (int i = 0; i < articles.length(); i++) {
                        JSONObject article = articles.getJSONObject(i);

                        String url = article.getString("url");
                        String imageUrl = article.getString("urlToImage");
                        String humanDate = article.getString("publishedAt");
                        String title = article.getString("title");
                        String description = article.getString("description");

                        NewsArticle newsArticle = new NewsArticle(url, imageUrl, humanDate, title, description);
                        mAdapter.addArticle(newsArticle);

                        saveToDb(newsArticle);
                    }
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "makeNewsApiRequest: e = ", e);
                    e.printStackTrace();
                    Toast.makeText(getActivity(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                hidepDialog();
            }
        });

        // Adding request to request queue
        App.getInstance().addToRequestQueue(jsonObjReq);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment2, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        MainActivity main = (MainActivity)getActivity();

        if (main.getScreenOrientation() == Configuration.ORIENTATION_PORTRAIT) {
            mLinearLayoutManager = new LinearLayoutManager(
                    getActivity(),
                    LinearLayoutManager.VERTICAL,
                    false
            );
        } else {
            mLinearLayoutManager = new LinearLayoutManager(
                    getActivity(),
                    LinearLayoutManager.HORIZONTAL,
                    false
            );
        }

        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        db = new DBHelper(getActivity());

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void showpDialog() {
        if(pDialog == null) {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
        }
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
