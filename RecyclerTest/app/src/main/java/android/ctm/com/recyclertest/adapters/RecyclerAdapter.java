package android.ctm.com.recyclertest.adapters;

import android.content.Context;
import android.content.Intent;
import android.ctm.com.recyclertest.App;
import android.ctm.com.recyclertest.R;
import android.ctm.com.recyclertest.activities.ArticleActivity;
import android.ctm.com.recyclertest.models.NewsArticle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import static android.ctm.com.recyclertest.activities.Consts.ARTICLE_KEY;

/**
 * Created by Paul on 4/12/17.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.PhotoHolder> {

    private static final String LOG_TAG = RecyclerAdapter.class.getName();

    private List<NewsArticle> mArticles;

    public RecyclerAdapter(List<NewsArticle> articles) {
        mArticles = articles;
    }

    public void addArticle(NewsArticle article) {
        mArticles.add(article);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerAdapter.PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item_row, parent, false);
        return new PhotoHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.PhotoHolder holder, int position) {
        NewsArticle itemPhoto = mArticles.get(position);
        holder.bindPhoto(itemPhoto);
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    //1
    public static class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //2
        private ImageView mItemImage;
        private TextView mItemDate;
        private TextView mItemDescription;
        private NewsArticle mArticle;

        //4
        public PhotoHolder(View v) {
            super(v);

            mItemImage = (ImageView) v.findViewById(R.id.item_image);
            mItemDate = (TextView) v.findViewById(R.id.item_date);
            mItemDescription = (TextView) v.findViewById(R.id.item_description);
            v.setOnClickListener(this);
        }

        //5
        @Override
        public void onClick(View v) {
            Context context = itemView.getContext();
            Intent showArticleIntent = new Intent(context, ArticleActivity.class);
            showArticleIntent.putExtra(ARTICLE_KEY, mArticle);
            context.startActivity(showArticleIntent);
        }

        /**
         *
         * @param article
         */
        public void bindPhoto(NewsArticle article) {
            mArticle = article;
            Glide.with(App.getInstance()).load(article.getImageUrl()).into(mItemImage);
            mItemDate.setText(article.getHumanDate());
            mItemDescription.setText(article.getDescription());
        }

    }
}

