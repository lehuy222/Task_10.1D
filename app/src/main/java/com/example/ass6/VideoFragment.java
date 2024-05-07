package com.example.ass6;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;

public class VideoFragment extends Fragment {

    private static final String VIDEO_URI = "video_uri";

    public static VideoFragment newInstance(String videoUri) {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putString(VIDEO_URI, videoUri);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        VideoView videoView = view.findViewById(R.id.videoView);
        if (getArguments() != null && getArguments().containsKey(VIDEO_URI)) {
            String videoUri = getArguments().getString(VIDEO_URI);
            videoView.setVideoURI(Uri.parse(videoUri));
            videoView.setOnCompletionListener(mp -> {
                // Handler to add a delay of 1 second after video completion
                new Handler().postDelayed(() -> {
                    // Close the fragment
                    if (getFragmentManager() != null) {
                        getFragmentManager().beginTransaction().remove(VideoFragment.this).commit();
                    }
                }, 1000); // 1000 milliseconds = 1 second
            });
            videoView.start();
        }
        return view;
    }
}
