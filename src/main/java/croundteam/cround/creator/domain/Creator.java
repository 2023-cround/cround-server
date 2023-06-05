package croundteam.cround.creator.domain;

import croundteam.cround.board.domain.Board;
import croundteam.cround.common.domain.BaseTime;
import croundteam.cround.creator.domain.platform.ActivityPlatforms;
import croundteam.cround.creator.domain.platform.Platform;
import croundteam.cround.creator.domain.platform.PlatformType;
import croundteam.cround.creator.domain.tag.CreatorTag;
import croundteam.cround.creator.domain.tag.CreatorTags;
import croundteam.cround.creator.domain.tag.Tags;
import croundteam.cround.member.domain.Member;
import croundteam.cround.member.domain.follow.Follow;
import croundteam.cround.member.domain.follow.Followers;
import croundteam.cround.shorts.domain.Shorts;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Table(uniqueConstraints = @UniqueConstraint(name = "creator_member_unique",columnNames = "member_id"),
        indexes = @Index(name = "idx_platform_activity_name",columnList = "platform_activity_name",unique = true))
// SELECT * FROM INFORMATION_SCHEMA.INDEXES where INDEX_NAME = 'IDX_PLATFORM_ACTIVITY_NAME';
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "description", "profileImage", "platform"})
public class Creator extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "creator_id")
    private Long id;

    @Embedded
    private ProfileImage profileImage;

    @Embedded
    private Description description;

    @Embedded
    private Platform platform;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_creator_to_member"))
    private Member member;

    @Embedded
    private CreatorTags creatorTags;

    @Embedded
    private ActivityPlatforms activityPlatforms;

    @Embedded
    private Followers followers;

    @Embedded
    private Boards boards;

    @Embedded
    private ShortClass shortClass;

    @Builder
    private Creator(ProfileImage profileImage, Description description, Member member, Platform platform,
                    Tags tags, ActivityPlatforms activityPlatforms) {
        this.profileImage = profileImage;
        this.description = description;
        this.member = member;
        this.platform = platform;
        this.creatorTags = castCreatorTagsFromTags(tags);
        this.activityPlatforms = activityPlatforms;
    }

    private CreatorTags castCreatorTagsFromTags(Tags tags) {
        return CreatorTags.create(this, tags);
    }

    public static Creator of(ProfileImage profileImage, Description description, Member member, Platform platform,
                             Tags tags, ActivityPlatforms activityPlatforms) {
        return Creator.builder()
                .profileImage(profileImage)
                .description(description)
                .member(member)
                .platform(platform)
                .tags(tags)
                .activityPlatforms(activityPlatforms)
                .build();
    }

    public void addMember(Member member) {
        this.member = member;
    }

    public void addBoard(Board board) {
        boards.add(board);
    }

    public void addTags(List<CreatorTag> creatorTags) {
        this.creatorTags = CreatorTags.create(creatorTags);
    }

    public void addShorts(Shorts shorts) {
        shortClass.add(shorts);
    }

    public void addFollow(Follow follow) {
        followers.add(follow);
    }

    public void removeFollow(Follow follow) {
        followers.remove(follow);
    }

    public int getFollowersCount() {
        return followers.getFollowersCount();
    }

    public boolean isFollowedBy(Member member) {
        if(Objects.isNull(member)) {
            return false;
        }
        return followers.isFollowedBy(this, member);
    }

    public String getActivityName() {
        return platform.getPlatformActivityName();
    }

    public String getPlatformType() {
        return platform.getPlatformType();
    }

    public String getPlatformTheme() {
        return platform.getPlatformTheme();
    }

    public Long getMemberId() {
        return member.getId();
    }

    public String getDescription() {
        return description.getDescription();
    }

    public String getProfileImage() {
        return profileImage.getProfileImage();
    }

    public List<PlatformType> getActivityPlatforms() {
        return activityPlatforms.getPlatformTypes();
    }

    public String getPlatformUrl() {
        return platform.getPlatformUrl();
    }

    public List<String> getTags() {
        return creatorTags.castTagsFromCreatorTags();
    }
}
