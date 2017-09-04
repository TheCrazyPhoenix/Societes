package io.github.thecrazyphoenix.societies.event;

import io.github.thecrazyphoenix.societies.api.society.Member;
import io.github.thecrazyphoenix.societies.api.event.MemberClaimChangeEvent;
import io.github.thecrazyphoenix.societies.api.land.MemberClaim;
import org.spongepowered.api.event.cause.Cause;

public class MemberClaimChangeEventImpl extends ClaimChangeEventImpl implements MemberClaimChangeEvent {
    private MemberClaim memberClaim;

    public MemberClaimChangeEventImpl(Cause cause, MemberClaim memberClaim) {
        super(cause, memberClaim.getParent());
        this.memberClaim = memberClaim;
    }

    @Override
    public MemberClaim getMemberClaim() {
        return memberClaim;
    }

    public static class Create extends MemberClaimChangeEventImpl implements MemberClaimChangeEvent.Create {
        public Create(Cause cause, MemberClaim memberClaim) {
            super(cause, memberClaim);
        }
    }

    public static class Destroy extends MemberClaimChangeEventImpl implements MemberClaimChangeEvent.Destroy {
        public Destroy(Cause cause, MemberClaim memberClaim) {
            super(cause, memberClaim);
        }
    }

    public static class ChangeOwner extends MemberClaimChangeEventImpl implements MemberClaimChangeEvent.ChangeOwner {
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
