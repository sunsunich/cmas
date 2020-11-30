INSERT INTO notifications_counter (diver_id, federationInitialCnt, mobileCnt, policyChangeCnt, unsubscribed)
SELECT id, 0, 0, 0, false
FROM divers;
update card_approval_requests set federationNotificationsCnt = 0