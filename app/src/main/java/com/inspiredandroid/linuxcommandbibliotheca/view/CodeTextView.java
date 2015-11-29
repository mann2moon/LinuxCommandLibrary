package com.inspiredandroid.linuxcommandbibliotheca.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.widget.TextView;

import com.inspiredandroid.linuxcommandbibliotheca.CommandManActivity;
import com.inspiredandroid.linuxcommandbibliotheca.R;
import com.inspiredandroid.linuxcommandbibliotheca.interfaces.ClickInterface;

/**
 * Created by Simon Schubert
 * <p/>
 * This View makes it very easy to highlightQueryInsideText defined commands in an normal textview. Define the
 * commands which should be highlighted in an string array and link it in the layout resource as
 * "command".
 */
public class CodeTextView extends TextView {

    String[] commands;

    public CodeTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CodeTextView);
        int resID = ta.getResourceId(R.styleable.CodeTextView_commands, R.array.default_codetextview_cmmands);
        commands = context.getResources().getStringArray(resID);
        ta.recycle();

        if (isInEditMode()) {
            return;
        }

        updateLinks();
        setMovementMethod(LinkMovementMethod.getInstance());
        setHighlightColor(Color.TRANSPARENT);
    }

    /**
     * Set clickable man pages(commands)
     *
     * @param commands
     */
    public void setCommands(String[] commands)
    {
        this.commands = commands;
        updateLinks();
    }

    /**
     * Mark man pages(commands) clickable
     */
    private void updateLinks()
    {
        setText(createSpannable(getText().toString(), commands));
    }

    /**
     * Highlights commands of the text and make them clickable
     *
     * @param text     spannable content
     * @param commands list of commands to highlightQueryInsideText
     * @return
     */
    private SpannableString createSpannable(String text, String[] commands)
    {
        SpannableString ss = new SpannableString(text);

        for (final String command : commands) {

            ClickInterface clickInterface = new ClickInterface() {
                @Override
                public void onClick()
                {
                    startCommandManActivity(command);
                }
            };

            ClickableTextView.addClickableSpanToPhrases(ss, text, command, clickInterface);
        }

        return ss;
    }

    /**
     * Show manual page
     *
     * @param command
     */
    private void startCommandManActivity(String command)
    {
        Intent intent = new Intent(getContext(), CommandManActivity.class);
        Bundle b = new Bundle();
        b.putString(CommandManActivity.EXTRA_COMMAND_NAME, command);
        intent.putExtras(b);

        getContext().startActivity(intent);
    }
}