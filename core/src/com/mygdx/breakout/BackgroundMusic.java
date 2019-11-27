package com.mygdx.breakout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BackgroundMusic {

    public Music generateInterstellar() {
        List<Music> album = new ArrayList<>();
        album.add(Gdx.audio.newMusic(Gdx.files.internal("music/interstellar/Dreaming_Of_The_Crash.mp3")));
        album.add(Gdx.audio.newMusic(Gdx.files.internal("music/interstellar/Cornfield_Chase.mp3")));
        album.add(Gdx.audio.newMusic(Gdx.files.internal("music/interstellar/Dust.mp3")));
        album.add(Gdx.audio.newMusic(Gdx.files.internal("music/interstellar/Day_One.mp3")));
        album.add(Gdx.audio.newMusic(Gdx.files.internal("music/interstellar/Stay.mp3")));
        album.add(Gdx.audio.newMusic(Gdx.files.internal("music/interstellar/Message_From_Home.mp3")));
        album.add(Gdx.audio.newMusic(Gdx.files.internal("music/interstellar/The_Wormhole.mp3")));
        album.add(Gdx.audio.newMusic(Gdx.files.internal("music/interstellar/Mountains.mp3")));
        album.add(Gdx.audio.newMusic(Gdx.files.internal("music/interstellar/Afraid_Of_Time.mp3")));
        album.add(Gdx.audio.newMusic(Gdx.files.internal("music/interstellar/A_Place_Among_The_Stars.mp3")));
        album.add(Gdx.audio.newMusic(Gdx.files.internal("music/interstellar/Running_Out.mp3")));
        album.add(Gdx.audio.newMusic(Gdx.files.internal("music/interstellar/I'm_Going_Home.mp3")));
        album.add(Gdx.audio.newMusic(Gdx.files.internal("music/interstellar/Coward.mp3")));
        album.add(Gdx.audio.newMusic(Gdx.files.internal("music/interstellar/Detach.mp3")));
        album.add(Gdx.audio.newMusic(Gdx.files.internal("music/interstellar/S.T.A.Y..mp3")));
        album.add(Gdx.audio.newMusic(Gdx.files.internal("music/interstellar/Where_We're_Going.mp3")));

        int rnd = new Random().nextInt(album.size());
        return album.get(rnd);
    }
}
