package com.emam8.emam8_universal.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.emam8.emam8_universal.Model.Poems;
import com.emam8.emam8_universal.R;
import com.emam8.emam8_universal.RecyclerPoetcontents;
import com.emam8.emam8_universal.ShowPoem;
import com.emam8.emam8_universal.ShowRawPoem;

import java.io.IOException;
import java.util.List;


public class PoemsAdapter extends RecyclerView.Adapter<PoemsAdapter.PoemViewHolder> {

    private List<Poems> poem;
    private String catid,gid,poet_id,mode;
    private MediaPlayer mediaPlayer;
    private Boolean isplay;
    Context mContext;


    public PoemsAdapter(List<Poems> poem, String catid, String gid, String poet_id,String mode, Context mContext) {
        this.poem = poem;
        this.catid = catid;
        this.gid = gid;
        this.poet_id = poet_id;
        this.mode = mode;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public PoemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list_poem,parent,false);

        return new PoemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PoemViewHolder holder, final int position) {
        final Poems poems=poem.get(position);
        final String poem_id=poems.getArticle_id();
        final String sabk=poems.getSabk();
        final String state=poems.getState();
        final String poet=poems.getPoet();
        final String profile=poems.getProfile();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);



        String title=poems.getTitle();
        String poet_name=poems.getPoet();
        if(poems.getPoet().length()>4)
        {
            title=title+"*"+poems.getPoet();
        }
        String profile_pic=poems.getProfile();
        if(profile_pic.length()<8)
        {
            profile_pic="images/icons/emam8_logo_orange.png";

        }
        String image_path="https://emam8.com/"+profile_pic;
        Uri uri = Uri.parse(String.valueOf(Uri.parse(image_path)));
        Glide.with(mContext).load(uri).circleCrop().into(holder.imgpoet);
        holder.imgpoet.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        if(sabk.length()>10){
            holder.img_play.setVisibility(View.VISIBLE);
            holder.img_play.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        }else{
            holder.img_play.setVisibility(View.GONE);
        }

        isplay=false;
        holder.img_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,sabk,Toast.LENGTH_SHORT).show();
                String audio_url="https://emam8.com/"+sabk;
//                mediaPlayer=MediaPlayer.create(mContext,audio_url);



                try {
                    if(!isplay){
                        holder.img_play.setImageResource(R.drawable.ic_pause_black_24dp);
                        mediaPlayer.setDataSource(audio_url);

                        mediaPlayer.prepare(); // might take long! (for buffering, etc)
                        mediaPlayer.start();

                    }else{
                        if(mediaPlayer.isPlaying()){
                            mediaPlayer.pause();
                            holder.img_play.setImageResource(R.drawable.ic_play_arrow_black_24dp);


                        }else{
                            mediaPlayer.start();
                            holder.img_play.setImageResource(R.drawable.ic_pause_black_24dp);


                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                isplay=true;









            }
        });

        holder.txtTitle.setText(title);
        holder.txt_poet.setText(poet_name);
        holder.imgpoet.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  Intent i;
                                                  String poetid=poems.getPoet_id();

                                                  String catid =PoemsAdapter.this.catid;
                                                  String mode = PoemsAdapter.this.mode;
                                                  String gid = PoemsAdapter.this.gid;
                                                  String poet_id = poetid;
//                                                  Toast.makeText(mContext,"Poet id : "+poetid+"catid="+catid,Toast.LENGTH_SHORT).show();
                                                  i=new Intent(mContext,RecyclerPoetcontents.class);
                                                  i.putExtra("poet_id", poet_id);

                                                  i.putExtra("catid",catid );
                                                  i.putExtra("mode", mode);
                                                  i.putExtra("gid", gid);
                                                  mContext.startActivity(i);
                                              }
                                          }
        );


        holder.txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(mContext,poems.getArticle_id()+"", Toast.LENGTH_SHORT).show();
        String sabk=poems.getSabk();
                Intent i;
        if(sabk.length()>10){

            i=new Intent(mContext,ShowPoem.class);
            i.putExtra("sabk", poems.getSabk());
            i.putExtra("article_id", poems.getArticle_id());
            i.putExtra("state", poems.getState());
            i.putExtra("poet", poems.getPoet());
            i.putExtra("title", poems.getTitle());
            mContext.startActivity(i);
        }else
        {
             i=new Intent(mContext, ShowRawPoem.class);
             i.putExtra("article_id", poems.getArticle_id());
             i.putExtra("state", poems.getState());
             i.putExtra("poet", poems.getPoet());
             i.putExtra("title", poems.getTitle());
            mContext.startActivity(i);
        }

//


            }
        });

    }

    @Override
    public int getItemCount() {
        return poem.size();
    }



    public class PoemViewHolder extends RecyclerView.ViewHolder{
        public TextView txtTitle,txt_poet;
        public ImageView imgpoet,img_play,img_fav;
        public RelativeLayout rltv;
        public PoemViewHolder(View itemView) {
            super(itemView);
            txtTitle=(TextView)itemView.findViewById(R.id.txt_title);
            txt_poet=(TextView)itemView.findViewById(R.id.txt_poet);
            imgpoet=(ImageView)itemView.findViewById(R.id.img_poet);
            rltv=(RelativeLayout)itemView.findViewById(R.id.rltv);
            img_play=(ImageView)itemView.findViewById(R.id.play_paus_btn);

        }
        public ImageView getImage(){ return this.imgpoet;}
    }
}
