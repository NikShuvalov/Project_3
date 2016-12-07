package shuvalov.nikita.restaurantroulette.Activities;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import shuvalov.nikita.restaurantroulette.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class UiTests {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void uiTests() {
        ViewInteraction actionMenuItemView = onView(
                isDisplayed());
        actionMenuItemView.perform(click());

        ViewInteraction appCompatSpinner = onView(
                 isDisplayed());
        appCompatSpinner.perform(click());

        ViewInteraction appCompatCheckedTextView = onView(
                allOf(withId(android.R.id.text1), withText("3"), isDisplayed()));
        appCompatCheckedTextView.perform(click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Navigate up"),

                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction cardView = onView(
                allOf(withId(R.id.roulette_card_holder),

                        isDisplayed()));
        cardView.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.roulette_query),

                        isDisplayed()));
        appCompatEditText.perform(replaceText("pizza"), closeSoftKeyboard());

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.roulette_button),

                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction relativeLayout = onView(
                allOf(
                        isDisplayed()));
        relativeLayout.check(matches(isDisplayed()));

        ViewInteraction relativeLayout2 = onView(

                        allOf(withId(R.id.mystery_restaurant),

                        isDisplayed()));
        relativeLayout2.check(matches(isDisplayed()));

        ViewInteraction relativeLayout3 = onView(
                allOf(
                        isDisplayed()));
        relativeLayout3.check(matches(isDisplayed()));

        ViewInteraction relativeLayout4 = onView(
                allOf(
                        isDisplayed()));
        relativeLayout4.check(matches(isDisplayed()));

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withContentDescription("Navigate up"),

                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction cardView2 = onView(
                allOf(withId(R.id.search_card_holder),

                        isDisplayed()));
        cardView2.perform(click());

        ViewInteraction relativeLayout5 = onView(
                allOf(withId(R.id.activity_search),

                        isDisplayed()));
        relativeLayout5.check(matches(isDisplayed()));

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.query_entry),

                        isDisplayed()));
        appCompatEditText2.perform(replaceText("pizza"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.basic_search), withText("Search"),

                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction relativeLayout6 = onView(
                allOf(withId(R.id.holder_layout),

                        isDisplayed()));
        relativeLayout6.check(matches(isDisplayed()));

        ViewInteraction relativeLayout7 = onView(
                allOf(withId(R.id.holder_layout),

                        isDisplayed()));
        relativeLayout7.perform(click());

        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.activity_detail),

                        isDisplayed()));
        linearLayout.check(matches(isDisplayed()));

        ViewInteraction imageView = onView(
                allOf(withId(R.id.business_image),

                        isDisplayed()));
        imageView.check(matches(isDisplayed()));

        ViewInteraction imageView2 = onView(
                allOf(withId(R.id.map_imageview),

                        isDisplayed()));
        imageView2.check(matches(isDisplayed()));

        ViewInteraction appCompatImageView = onView(
                withId(R.id.share_image_view));
        appCompatImageView.perform(scrollTo(), click());

        ViewInteraction relativeLayout8 = onView(
                allOf(withId(R.id.activity_share),

                        isDisplayed()));
        relativeLayout8.check(matches(isDisplayed()));

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withContentDescription("Navigate up"),

                        isDisplayed()));
        appCompatImageButton3.perform(click());

        ViewInteraction appCompatImageButton4 = onView(
                allOf(withContentDescription("Navigate up"),

                        isDisplayed()));
        appCompatImageButton4.perform(click());

        ViewInteraction appCompatImageButton5 = onView(
                allOf(withContentDescription("Navigate up"),

                        isDisplayed()));
        appCompatImageButton5.perform(click());

        ViewInteraction cardView3 = onView(
                allOf(withId(R.id.date_night_card_holder),

                        isDisplayed()));
        cardView3.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.query_entry), isDisplayed()));
        appCompatEditText3.perform(replaceText("11226"), closeSoftKeyboard());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.search_button), withText("Search"), isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatImageView2 = onView(
                allOf(withId(R.id.add_button),

                        isDisplayed()));
        appCompatImageView2.perform(click());

        ViewInteraction relativeLayout9 = onView(
                allOf(withId(R.id.holder_layout),

                        isDisplayed()));
        relativeLayout9.check(matches(isDisplayed()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.prompt_text), withText("Alright, \n what's up next?"),

                        isDisplayed()));
        textView.check(matches(withText("Alright,   what's up next?")));

        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.finalize),

                        isDisplayed()));
        floatingActionButton2.perform(click());

        ViewInteraction relativeLayout10 = onView(
                allOf(
                        isDisplayed()));
        relativeLayout10.check(matches(isDisplayed()));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
