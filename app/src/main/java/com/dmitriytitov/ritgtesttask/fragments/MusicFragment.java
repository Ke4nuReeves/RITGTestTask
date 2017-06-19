package com.dmitriytitov.ritgtesttask.fragments;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dmitriytitov.ritgtesttask.Constants;
import com.dmitriytitov.ritgtesttask.R;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 */
public class MusicFragment extends Fragment {

    private final static String AUDIO_FILE_NAME = "epic_sax_guy.mp3";
    private final static String AUDIO_DIR_NAME = "/RITGTestTaskCache";

    private MediaPlayer mediaPlayer;
    private File audioFile;
    private boolean onPause = false;

    public MusicFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_music, container, false);
        ImageButton playButton = (ImageButton) rootView.findViewById(R.id.play_button);
        ImageButton pauseButton = (ImageButton) rootView.findViewById(R.id.pause_button);

        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + AUDIO_DIR_NAME);
        dir.mkdirs();
        audioFile = new File(dir, AUDIO_FILE_NAME);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (mediaPlayer == null || !mediaPlayer.isPlaying()) {
                        if (audioFile.exists()) {
                            if (onPause) {
                                mediaPlayer.start();
                                onPause = false;
                            } else {
                                releaseMP();
                                mediaPlayer = new MediaPlayer();
                                mediaPlayer.setDataSource(audioFile.getPath());
                                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                mediaPlayer.prepare();
                                mediaPlayer.start();
                                onPause = false;
                            }
                        } else {
                            releaseMP();
                            mediaPlayer = new MediaPlayer();
                            new RequestMusicFile().execute();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    onPause = true;
                }
            }
        });


        return rootView;
    }

    private class RequestMusicFile extends AsyncTask<Void, Void, Boolean> {

        RestTemplate restTemplate;
        HttpEntity<String> entity;
        ResponseEntity<byte[]> response;

        @Override
        protected void onPreExecute() {
            restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
            entity = new HttpEntity<>(headers);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                response = restTemplate.exchange(Constants.URL.GET_MUSIC,
                        HttpMethod.GET, entity, byte[].class, "1");

            } catch (RestClientException ex) {

                return false;
            }

            return response.getStatusCode() == HttpStatus.OK;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (!success) {
                Toast toast = Toast.makeText(getContext(), "Connection failed", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                try {
                    FileOutputStream fos = new FileOutputStream(audioFile);
                    byte[] music = response.getBody();
                    fos.write(music);
                    fos.close();

                    mediaPlayer.setDataSource(audioFile.getPath());
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.prepare();
                    mediaPlayer.start();

                } catch (IOException e) {

                    e.printStackTrace();
                    throw new RuntimeException("IOError writing to file");
                }
            }
        }
    }

    private void releaseMP() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseMP();
    }
}
