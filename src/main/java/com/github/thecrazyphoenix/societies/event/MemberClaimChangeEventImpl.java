package com.github.thecrazyphoenix.societies.event;

import com.github.thecrazyphoenix.societies.api.society.Member;
import com.github.thecrazyphoenix.societies.api.society.Society;
import com.github.thecrazyphoenix.societies.api.event.MemberClaimChangeEvent;
import com.github.thecrazyphoenix.societies.api.land.Claim;
import com.github.thecrazyphoenix.societies.api.land.MemberClaim;
import org.spongepowered.api.event.cause.Cause;

public class MemberClaimChangeEventImpl extends ClaimChangeEventImpl implements MemberClaimChangeEvent {
    private MemberClaim memberClaim;

    public MemberClaimChangeEventImpl(Cause cause, Society society, Claim claim, MemberClaim memberClaim) {
        super(cause, society, claim);
        this.memberClaim = memberClaim;
    }

    @Override
    public MemberClaim getMemberClaim() {
        return memberClaim;
    }

    public static class Create extends MemberClaimChangeEventImpl implements MemberClaimChangeEvent.Create {
        public Create(Cause cause, Society society, Claim claim, MemberClaim memberClaim) {
            super(cause, society, claim, memberClaim);
        }
    }

    public static class Destroy extends MemberClaimChangeEventImpl implements MemberClaimChangeEvent.Destroy {
        public Destroy(Cause cause, Society society, Claim claim, MemberClaim memberClaim) {
            super(cause, society, claim, memberClaim);
        }
    }

    public static class ChangeOwner extends MemberClaimChangeEventImpl implements MemberClaimChangeEvent.ChangeOwner {
        private Member newOwner;

        public ChangeOwner(Cause cause, Society society, Claim claim, MemberClaim memberClaim, Member newOwner) {
            super(cause, society, claim, memberClaim);
            this.newOwner = newOwner;
        }

        @Override
        public Member getNewOwner() {
            return newOwner;
        }
    }
}
