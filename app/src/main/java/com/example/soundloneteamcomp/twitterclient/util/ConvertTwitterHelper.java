package com.example.soundloneteamcomp.twitterclient.util;

import com.example.soundloneteamcomp.twitterclient.model.TweetModel;
import com.example.soundloneteamcomp.twitterclient.model.UserModel;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;

import java.util.ArrayList;
import java.util.List;


public class ConvertTwitterHelper {
    public UserModel ConvertUser(User user){
        return new UserModel(user.contributorsEnabled,user.createdAt,user.defaultProfile,user.defaultProfileImage,
                user.description,user.email,user.entities,user.favouritesCount,user.followRequestSent,user.followersCount,user.friendsCount,
                user.geoEnabled,user.id,user.idStr,user.isTranslator,user.lang,user.listedCount,user.location,user.name,user.profileBackgroundColor,
                user.profileBackgroundImageUrl,user.profileBackgroundImageUrlHttps,user.profileBackgroundTile,user.profileBannerUrl,user.profileImageUrl,
                user.profileImageUrlHttps,user.profileLinkColor,user.profileSidebarBorderColor,user.profileSidebarFillColor,user.profileTextColor,
                user.profileUseBackgroundImage,user.protectedUser,user.screenName,user.showAllInlineMedia,user.status,user.statusesCount,user.timeZone
                ,user.url,user.utcOffset,user.verified,user.withheldInCountries,user.withheldScope);
    }

    public TweetModel ConvertTweet(Tweet tweet){
        return new TweetModel(tweet.coordinates,tweet.createdAt,tweet.currentUserRetweet,tweet.entities,
                tweet.extendedEntities,tweet.favoriteCount,tweet.favorited,tweet.filterLevel,tweet.id,
                tweet.idStr,tweet.inReplyToScreenName,tweet.inReplyToStatusId,tweet.inReplyToStatusIdStr,
                tweet.inReplyToUserId,tweet.inReplyToUserIdStr,tweet.lang,tweet.place,tweet.possiblySensitive,
                tweet.scopes,tweet.quotedStatusId,tweet.quotedStatusIdStr,tweet.quotedStatus,tweet.retweetCount,
                tweet.retweeted,tweet.retweetedStatus,tweet.source,tweet.text,tweet.displayTextRange,tweet.truncated,ConvertUser(tweet.user),
                tweet.withheldCopyright,tweet.withheldInCountries,tweet.withheldScope,tweet.card);
    }

    public List<TweetModel> ConvertList(List<Tweet> tweetList){
        List<TweetModel> modelList = new ArrayList<>();
        for (Tweet tweet : tweetList)
            modelList.add(ConvertTweet(tweet));
        return modelList;
    }

}
