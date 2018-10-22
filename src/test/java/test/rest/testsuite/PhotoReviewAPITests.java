package test.rest.testsuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.rest.review.DeleteReviewRecommendTest;
import test.rest.review.PostRecommendReviewTest;
import test.rest.review.myreview.GetMyRecommendationReviewListTest;
import test.rest.review.myreview.GetMyReviewListTest;
import test.rest.review.myreview.GetSearchReviewTest;

@RunWith(Suite.class)
@SuiteClasses({ DeleteReviewRecommendTest.class, PostRecommendReviewTest.class, GetMyRecommendationReviewListTest.class,
		GetMyReviewListTest.class, GetSearchReviewTest.class })
public class PhotoReviewAPITests {

}
