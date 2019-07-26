package com.example.priyandubey.musicplayer;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class GameFragment extends Fragment {

    Button playAgainButtons ;
    Button playAgainButton ;
    TextView winnerTextViews;
    TextView winnerTextView;
    GridLayout gridLayout;
    Button splayAgainButton;
    TextView swinnerTextView;

    public GameFragment() {
        // Required empty public constructor
    }


    int enteries = 0;

    int[] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2};

    int[][] winningPositions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};

    int activePlayer = 0;

    boolean gameActive = true;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fview = inflater.inflate(R.layout.fragment_game, container, false);

        playAgainButtons = (Button) fview.findViewById(R.id.playAgainButton);
        gridLayout = (GridLayout) fview.findViewById(R.id.gridLayout);
        winnerTextViews = (TextView) fview.findViewById(R.id.winnerTextView);
        playAgainButton = (Button) fview.findViewById(R.id.playAgainButton);
        winnerTextView = (TextView) fview.findViewById(R.id.winnerTextView);
        splayAgainButton = (Button) fview.findViewById(R.id.playAgainButton);
        swinnerTextView = (TextView) fview.findViewById(R.id.winnerTextView);

        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playAgain(view);
            }
        });

        ImageView imageView1 = fview.findViewById(R.id.imageView1);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dropIn(view);
            }
        });
        ImageView imageView2 = fview.findViewById(R.id.imageView2);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dropIn(view);
            }
        });
        ImageView imageView3 = fview.findViewById(R.id.imageView3);
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dropIn(view);
            }
        });
        ImageView imageView4 = fview.findViewById(R.id.imageView4);
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dropIn(view);
            }
        });
        ImageView imageView5 = fview.findViewById(R.id.imageView5);
        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dropIn(view);
            }
        });
        ImageView imageView6 = fview.findViewById(R.id.imageView6);
        imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dropIn(view);
            }
        });
        ImageView imageView7 = fview.findViewById(R.id.imageView7);
        imageView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dropIn(view);
            }
        });
        ImageView imageView8 = fview.findViewById(R.id.imageView8);
        imageView8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dropIn(view);
            }
        });
        ImageView imageView9 = fview.findViewById(R.id.imageView9);
        imageView9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dropIn(view);
            }
        });


        return fview;
    }

    public void dropIn(View view) {

        ImageView counter = (ImageView) view;

        int tappedCounter = Integer.parseInt(counter.getTag().toString());

        if (gameState[tappedCounter] == 2 && gameActive) {

            enteries += 1;

            gameState[tappedCounter] = activePlayer;


            if (activePlayer == 0) {

                counter.setImageResource(R.drawable.yellow);

                activePlayer = 1;

            } else {

                counter.setImageResource(R.drawable.red);

                activePlayer = 0;

            }


            for (int[] winningPosition : winningPositions) {

                if (gameState[winningPosition[0]] == gameState[winningPosition[1]] && gameState[winningPosition[1]] == gameState[winningPosition[2]] && gameState[winningPosition[0]] != 2) {

                    // Somone has won!

                    gameActive = false;

                    String winner = "";

                    if (activePlayer == 1) {

                        winner = "CROSS";

                    } else {

                        winner = "KNOT";

                    }
                    winnerTextView.setText(winner + " has won!");

                    playAgainButton.setVisibility(View.VISIBLE);

                    winnerTextView.setVisibility(View.VISIBLE);

                }

            }

            if(enteries == 9 && gameActive == true){

                winnerTextView.setText("Match has drawn");

                playAgainButton.setVisibility(View.VISIBLE);

                winnerTextView.setVisibility(View.VISIBLE);

            }
        }
    }
    public void playAgain(View view) {

        enteries = 0;

        playAgainButton.setVisibility(View.INVISIBLE);

        winnerTextView.setVisibility(View.INVISIBLE);

        for(int i=0; i<gridLayout.getChildCount(); i++) {

            ImageView counter = (ImageView) gridLayout.getChildAt(i);

            counter.setImageDrawable(null);

        }

        for (int i=0; i<gameState.length; i++) {

            gameState[i] = 2;

        }

        activePlayer = 0;

        gameActive = true;

    }

}
