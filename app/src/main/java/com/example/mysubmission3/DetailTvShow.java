package com.example.mysubmission3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.mysubmission3.model.Movie;
import com.example.mysubmission3.model.Tvshow;

public class DetailTvShow extends AppCompatActivity {
    public static final String EXTRA_TVSHOW = "EXTRA TVSHOW";
    public static String url = "https://image.tmdb.org/t/p/w342";
    private TextView jdl, desc;
    private ImageView img;
    private ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tv_show);
        Toolbar tbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.title));
        getSupportActionBar().setElevation(0);
        pb = (ProgressBar)findViewById(R.id.pbar);
        Tvshow tvs = getIntent().getParcelableExtra(EXTRA_TVSHOW);
        jdl = findViewById(R.id.text_title);
        desc = findViewById(R.id.text_description);
        img = findViewById(R.id.image);

        jdl.setText(tvs.getJudul());
        desc.setText(tvs.getDesc());
        Glide.with(this)
                .load(url + tvs.getPoster_path())
                .placeholder(R.color.colorAccent)
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        pb.setVisibility(View.GONE);
                        Toast.makeText(DetailTvShow.this, "Gagal Load Image ", Toast.LENGTH_LONG).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        pb.setVisibility(View.GONE);
                        return false;
                    }
                })
                .dontAnimate()
                .into(img);
    }
}
