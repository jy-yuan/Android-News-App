package com.example.yuanjiayi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.net.URL;

public class NewsDetailActivivty extends AppCompatActivity
        implements AppBarLayout.OnOffsetChangedListener{
    private int whichImg = 0;
    private ImageView imageView;
    private TextView appbar_title, appbar_subtitle, date, time, title, content;
    private boolean isHideToolbarView = false;
    private FrameLayout date_behavior;
    private LinearLayout titleAppbar;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private String mUrl, mImg, mTitle, mDate, mSource, mAuthor, mContent, mVideo;
    private String[] mImgUrls;
    private boolean isFav;
    private int mPosition;
    private ScrollView scrollView;

    private Activity self;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        self = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        Intent intent = getIntent();
        mUrl = intent.getStringExtra("url");
        mImg = intent.getStringExtra("img");
        mTitle = intent.getStringExtra("title");
        mContent = intent.getStringExtra("content");
        mDate = intent.getStringExtra("date");
        mSource = intent.getStringExtra("source");
        mAuthor = intent.getStringExtra("author");
        mImgUrls = intent.getStringArrayExtra("imgUrls");
        isFav = intent.getBooleanExtra("isfav", false);
        mPosition = intent.getIntExtra("position", 0);
        mVideo = intent.getStringExtra("video");

        if(!mVideo.equals("")){
            try {
                URL url = new URL(mVideo);
                Uri uri = Uri.parse(url.toURI().toString());
                final VideoView vv = findViewById(R.id.videoview);
                vv.setVideoURI(uri);
                vv.getLayoutParams().height = (int) (256 * getResources().getDisplayMetrics().density);
                vv.requestFocus();
                vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    public void onPrepared(MediaPlayer mp) {
                        vv.start();
                    }
                });
                MediaController controller = new MediaController(this);
                vv.setMediaController(controller);
                controller.setAnchorView(vv);
            } catch (Exception e) {
                System.out.println("can't play this video");
            }
        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");

        appBarLayout = findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(this);
        date_behavior = findViewById(R.id.date_behavior);
        titleAppbar = findViewById(R.id.title_appbar);
        imageView = findViewById(R.id.backdrop);
        appbar_title = findViewById(R.id.title_on_appbar);
        appbar_subtitle = findViewById(R.id.subtitle_on_appbar);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        title = findViewById(R.id.title);
        content = findViewById(R.id.news_content);

        mContent = "\n" + mContent + "\n";

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(Utils.getRandomDrawbleColor());

        Glide.with(this)
                .load(mImg)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);

        appbar_title.setText(mSource);
        appbar_subtitle.setText(mUrl);
        date.setText(Utils.DateFormat(mDate));
        title.setText(mTitle);
        content.setText(mContent);

        String author;
        if (mAuthor != null){
            author = " \u2022 " + mAuthor;
        } else {
            author = "";
        }

        time.setText(mSource + author + " \u2022 " + mDate);

        if(mImgUrls != null && mImgUrls.length > 1) {

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (whichImg < mImgUrls.length - 1) {
                        whichImg++;
                    } else {
                        whichImg = 0;
                    }
                    Glide.with(self)
                            .load(mImgUrls[whichImg])
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .into(imageView);
                }

            });

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScrool = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScrool;

        if(percentage == 1f && isHideToolbarView) {
            date_behavior.setVisibility(View.GONE);
            titleAppbar.setVisibility(View.VISIBLE);
            isHideToolbarView = !isHideToolbarView;
        } else if(percentage < 1f && !isHideToolbarView) {
            date_behavior.setVisibility(View.VISIBLE);
            titleAppbar.setVisibility(View.GONE);
            isHideToolbarView = !isHideToolbarView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news, menu);
        MenuItem Fav = menu.getItem(1);
        if(isFav){
            Fav.setIcon(R.drawable.ic_favorite_24dp);
        } else {
            Intent intent = new Intent();
            intent.putExtra("Fav", isFav);
            intent.putExtra("Position", mPosition);
            setResult(1, intent);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.share){
//            int h = 0;
//            Bitmap bitmap = null;
//            for (int i = 0; i < scrollView.getChildCount(); i++) {
//                h += scrollView.getChildAt(i).getHeight();
//                scrollView.getChildAt(i).setBackgroundColor(Color.parseColor("#ffffff"));
//            }
//            bitmap = Bitmap.createBitmap(scrollView.getWidth(), h, Bitmap.Config.RGB_565);
//            final Canvas canvas = new Canvas(bitmap);
//            scrollView.draw(canvas);

            try{
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, mSource);
                String body = "分享自 Naïve News";
                if(mImgUrls != null)
                    body = "封面图:" + mImgUrls[0] + "\n";
                body = mTitle + "\n" +"原文:" + mUrl + "\n" + body;
                i.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(i, "分享"));
            } catch (Exception e) {
                Toast.makeText(this, "分享失败", Toast.LENGTH_SHORT).show();
            }
            return super.onOptionsItemSelected(item);
        } else if(id == R.id.favorite){
            if(isFav) {
                isFav = !isFav;
                item.setIcon(R.drawable.ic_favorite_border_24dp);
            } else {
                isFav = !isFav;
                item.setIcon(R.drawable.ic_favorite_24dp);
            }
            Intent intent = new Intent();
            intent.putExtra("Fav", isFav);
            intent.putExtra("Position", mPosition);
            setResult(1, intent);
        } else if(id == android.R.id.home){
            this.finish();
        }
        return true;
    }


}
