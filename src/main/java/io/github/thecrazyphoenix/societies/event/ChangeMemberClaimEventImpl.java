package io.github.thecrazyphoenix.societies.event;

import io.github.thecrazyphoenix.societies.api.society.Member;
import io.github.thecrazyphoenix.societies.api.event.ChangeMemberClaimEvent;
import io.github.thecrazyphoenix.societies.api.society.MemberClaim;
import org.spongepowered.api.event.cause.Cause;

public class ChangeMemberClaimEventImpl extends ChangeCuboidEventImpl implements ChangeMemberClaimEvent {
    private MemberClaim memberClaim;

    public ChangeMemberClaimEventImpl(Cause cause, MemberClaim memberClaim) {
        super(cause, memberClaim);
        this.memberClaim = memberClaim;
    }

    @Override
    public MemberClaim getMemberClaim() {
        return memberClaim;
    }

    public static class Create extends ChangeMemberClaimEventImpl implements ChangeMemberClaimEvent.Create {
        public Create(Cause cause, MemberClaim memberClaim) {
            super(cause, memberClaim);
        }
    }

    public static class Destroy extends ChangeMemberClaimEventImpl implements ChangeMemberClaimEvent.Destroy {
        public Destroy(Cause cause, MemberClaim memberClaim) {
            super(cause, memberClaim);
        }
    }

    public static class ChangeOwner extends ChangeMemberClaimEventImpl implements ChangeMemberClaimEvent.ChangeOwner {
        private Member newOwner;

        public ChangeOwner(Cause cause, MemberClaim memberClaim, Member newOwner) {
            super(cause, memberClaim);
            this.newOwner = newOwner;
        }

        @Override
        public Member getNewOwner() {
            return newOwner;
        }
    }
}
