kafkaDir='/Users/alscott/RedHatTech/kafka_2.12-2.5.0.redhat-00003'
cd $ kafkaDir

## Operational Topics for Platform
bin/kafka-topics.sh --delete --topic opsmgmt_platformtransactions &
## HL7
## Inbound to iDAAS Platform by Message Trigger
## Facility: MCTN
## Application: MMS
bin/kafka-topics.sh --delete --topic mctn_mms_adt &
bin/kafka-topics.sh --delete --topic mctn_mms_orm &
bin/kafka-topics.sh --delete --topic mctn_mms_oru &
bin/kafka-topics.sh --delete --topic mctn_mms_rde &
bin/kafka-topics.sh --delete --topic mctn_mms_sch &
bin/kafka-topics.sh --delete --topic mctn_mms_vxu &
bin/kafka-topics.sh --delete --topic mctn_mms_mfn &
bin/kafka-topics.sh --delete --topic mctn_mms_mdm &
## HL7
## Facility By Application by Message Trigger
## Facility: MCTN
## Application: MMS
bin/kafka-topics.sh --delete --topic mctn_adt &
bin/kafka-topics.sh --delete --topic mctn_orm &
bin/kafka-topics.sh --delete --topic mctn_oru &
bin/kafka-topics.sh --delete --topic mctn_rde &
bin/kafka-topics.sh --delete --topic mctn_sch &
bin/kafka-topics.sh --delete --topic mctn_vxu &
bin/kafka-topics.sh --delete --topic mctn_mfn &
bin/kafka-topics.sh --delete --topic mctn_mdm &
## HL7
## Enterprise By Application by Message Trigger
## Facility: MCTN
## Application: MMS
bin/kafka-topics.sh --delete --topic mms_adt &
bin/kafka-topics.sh --delete --topic mms_orm &
bin/kafka-topics.sh --delete --topic mms_oru &
bin/kafka-topics.sh --delete --topic mms_rde &
bin/kafka-topics.sh --delete --topic mms_sch &
bin/kafka-topics.sh --delete --topic mms_vxu &
bin/kafka-topics.sh --delete --topic mms_mfn &
bin/kafka-topics.sh --delete --topic mms_mdm &
## HL7
## Enterprise by Message Trigger
## Application: MMS
bin/kafka-topics.sh --delete --topic ent_adt &
bin/kafka-topics.sh --delete --topic ent_orm &
bin/kafka-topics.sh --delete --topic ent_oru &
bin/kafka-topics.sh --delete --topic ent_rde &
bin/kafka-topics.sh --delete --topic ent_sch &
bin/kafka-topics.sh --delete --topic ent_vxu &
bin/kafka-topics.sh --delete --topic ent_mfn &
bin/kafka-topics.sh --delete --topic ent_mdm &
