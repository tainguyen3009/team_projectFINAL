package algonquin.cst2335.team_project;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * The main activity of the application, providing navigation to the dictionary feature.
 *
 * @author Tai Nguyen
 * 04086103
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView goToDictionaryButton = findViewById(R.id.dictionaryApp);
        TextView dictionaryTitle = findViewById(R.id.dictionaryTitle);
        chooseAnimation(goToDictionaryButton);

        goToDictionaryButton.setOnClickListener(click -> {
            styleText(dictionaryTitle);
            Intent goToDictionaryApp = new Intent(this, MainActivityDictionary.class);
            startActivity(goToDictionaryApp);
        });
    }

    /**
     * Styles the given TextView by changing text color, size, and applying bold style.
     *
     * @param textView The TextView to style.
     */
    private void styleText(TextView textView) {
        textView.setTextColor(Color.RED);
        textView.setTextSize(14);  // Change the text size
        textView.setTypeface(null, Typeface.BOLD);  // Apply bold style

    }

    /**
     * Rotates the given ImageView continuously.
     *
     * @param image The ImageView to rotate.
     */
    private void rotateElements(ImageView image) {
        RotateAnimation anim = new RotateAnimation(0f, 270f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(2500);
        image.startAnimation(anim);
    }

    /**
     * Creates a flashing animation for the given ImageView.
     *
     * @param image The ImageView to flash.
     */
    public void flashingAnimation(ImageView image) {
        // Create ObjectAnimator for alpha property
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(image, "alpha", 1f, 0f);
        alphaAnimator.setDuration(500); // Set the duration of each half of the flashing animation
        alphaAnimator.setRepeatMode(ObjectAnimator.REVERSE);
        alphaAnimator.setRepeatCount(ObjectAnimator.INFINITE);

        // Start the flashing animation
        alphaAnimator.start();
    }

    /**
     *  Creates a fly-in animation for the given ImageView.
     *
     *  @param view The ImageView to fly in.
     */
    private void flyInAnimation(ImageView view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", -18f, 0f);
        animator.setDuration(1000);
        animator.setRepeatMode(ObjectAnimator.REVERSE);
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.start();
    }

    /**
     * Creates a fly-out animation for the given View.
     *
     * @param view The View to fly out.
     */
    private void flyOutAnimation(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", 18f, 0f);
        animator.setDuration(1000);
        animator.setRepeatMode(ObjectAnimator.REVERSE);
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.start();
    }

    /**
     * Chooses and applies a random animation for the given ImageView based on a generated random integer.
     *
     * @param view The ImageView to apply the animation.
     */
    private void chooseAnimation(ImageView view) {
        RandomInteger Random = new RandomInteger();
        int RandomInteger = Random.generateRandomInt();
        if(RandomInteger == 1) {
            flashingAnimation(view);
        }

        if(RandomInteger == 2) {
            rotateElements(view);
        }

        if(RandomInteger == 3) {
            flashingAnimation(view);
        }
        if(RandomInteger == 4) {
            flyOutAnimation(view);
        }



    }
}