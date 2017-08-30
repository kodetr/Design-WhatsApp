package id.codetr.whatappedittext;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import id.codetr.whatappedittext.Widget.WhatsappViewCompat;

public class MainActivity extends AppCompatActivity {

    private LinearLayout mContainer;
    private EmojiconEditText mEditText;
    private AnimButton animButton;

    private View theMenu;
    private View menu1;
    private View menu2;
    private View menu3;
    private View menu4;
    private View menu5;
    private View menu6;
    private View overlay;

    private boolean menuOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(" CodeTR");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("CodeTR");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        theMenu = findViewById(R.id.the_menu);
        menu1 = findViewById(R.id.menu1);
        menu2 = findViewById(R.id.menu2);
        menu3 = findViewById(R.id.menu3);
        menu4 = findViewById(R.id.menu4);
        menu5 = findViewById(R.id.menu5);
        menu6 = findViewById(R.id.menu6);
        overlay = findViewById(R.id.overlay);

        mContainer = (LinearLayout) findViewById(R.id.container);
        mEditText = (EmojiconEditText) findViewById(R.id.whatsapp_edit_view);
        animButton = (AnimButton) findViewById(R.id.send_button);

        WhatsappViewCompat.applyFormatting(mEditText);

        findViewById(R.id.send_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addView();
            }
        });

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                animButton.goToState(text.length() == 0 ? AnimButton.FIRST_STATE : AnimButton.SECOND_STATE);
            }
        });
    }

    private void addView() {

        String text = mEditText.getText().toString();

        TextView textView = new TextView(this);

        textView.setBackgroundResource(R.drawable.ic_whatsapp_chathead);
        textView.setText(text);
        textView.setPadding(10, 10, 40, 10);
        mContainer.addView(textView);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) textView.getLayoutParams();
        layoutParams.gravity = Gravity.END;
        layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.topMargin = layoutParams.rightMargin = layoutParams.bottomMargin = layoutParams.leftMargin = 20;
        textView.setGravity(Gravity.START | Gravity.CENTER);
        textView.setLayoutParams(layoutParams);

        WhatsappViewCompat.applyFormatting(textView);

        mEditText.setText("");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_languages) {

            if (!menuOpen) {
                revealMenu();
            } else {
                hideMenu();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void revealMenu() {
        menuOpen = true;

        theMenu.setVisibility(View.INVISIBLE);
        int cx = theMenu.getRight() - 200;
        int cy = theMenu.getTop();
        int finalRadius = Math.max(theMenu.getWidth(), theMenu.getHeight());
        Animator anim =
                ViewAnimationUtils.createCircularReveal(theMenu, cx, cy, 0, finalRadius);
        anim.setDuration(600);
        theMenu.setVisibility(View.VISIBLE);
        overlay.setVisibility(View.VISIBLE);
        anim.start();


        // Animate The Icons One after the other, I really would like to know if there is any
        // simpler way to do it
        Animation popeup1 = AnimationUtils.loadAnimation(this, R.anim.popeup);
        Animation popeup2 = AnimationUtils.loadAnimation(this, R.anim.popeup);
        Animation popeup3 = AnimationUtils.loadAnimation(this, R.anim.popeup);
        Animation popeup4 = AnimationUtils.loadAnimation(this, R.anim.popeup);
        Animation popeup5 = AnimationUtils.loadAnimation(this, R.anim.popeup);
        Animation popeup6 = AnimationUtils.loadAnimation(this, R.anim.popeup);
        popeup1.setStartOffset(50);
        popeup2.setStartOffset(100);
        popeup3.setStartOffset(150);
        popeup4.setStartOffset(200);
        popeup5.setStartOffset(250);
        popeup6.setStartOffset(300);
        menu1.startAnimation(popeup1);
        menu2.startAnimation(popeup2);
        menu3.startAnimation(popeup3);
        menu4.startAnimation(popeup4);
        menu5.startAnimation(popeup5);
        menu6.startAnimation(popeup6);

    }

    public void hideMenu() {
        menuOpen = false;
        int cx = theMenu.getRight() - 200;
        int cy = theMenu.getTop();
        int initialRadius = theMenu.getWidth();
        Animator anim = ViewAnimationUtils.createCircularReveal(theMenu, cx, cy, initialRadius, 0);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                theMenu.setVisibility(View.INVISIBLE);
                theMenu.setVisibility(View.GONE);
                overlay.setVisibility(View.INVISIBLE);
                overlay.setVisibility(View.GONE);
            }
        });
        anim.start();
    }

    @Override
    public void onBackPressed() {
        if (menuOpen) {
            hideMenu();
        } else {
            finishAfterTransition();
        }
    }

    public void overlayClick(View v) {
        hideMenu();
    }

    public void menuClick(View view) {
        hideMenu();

        if (view.getTag().equals("Galley")) {
            Toast.makeText(this, "Galley", Toast.LENGTH_LONG).show();
        } else if (view.getTag().equals("Photo")) {
            Toast.makeText(this, "Photo", Toast.LENGTH_LONG).show();
        } else if (view.getTag().equals("Vidio")) {
            Toast.makeText(this, "Vidio", Toast.LENGTH_LONG).show();
        }

    }

}
