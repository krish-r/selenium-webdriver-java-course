#!/usr/bin/bash

###################################################################################################
#     This is extracting the values for each test suite description.                              #
#     It uses awk to print the value between two quotes.                                          #
#     The columns printed is based on the following example string:                               #
#     <testng-results ignored="0" total="23" passed="22" failed="1" skipped="0">                  #
###################################################################################################

TEST_RESULTS_LOCATION="${1:-/home/runner/work/selenium-webdriver-java-course/selenium-webdriver-java-course/webdriver_java/target/surefire-reports}"
TEST_RESULTS_STRING=$(cat "${TEST_RESULTS_LOCATION}/testng-results.xml" | grep "<testng-results")
# echo "github_actions_ignored_tests=$(echo ${TEST_RESULTS_STRING} | awk -F'"' '{ print $2 }')" | curl --data-binary @- ${PUSHGATEWAY_URL}/metrics/job/github_actions
# echo "github_actions_total_tests=$(echo ${TEST_RESULTS_STRING} | awk -F'"' '{ print $4 }')" | curl --data-binary @- ${PUSHGATEWAY_URL}/metrics/job/github_actions
# echo "github_actions_passed_tests=$(echo ${TEST_RESULTS_STRING} | awk -F'"' '{ print $6 }')" | curl --data-binary @- ${PUSHGATEWAY_URL}/metrics/job/github_actions
# echo "github_actions_failed_tests=$(echo ${TEST_RESULTS_STRING} | awk -F'"' '{ print $8 }')" | curl --data-binary @- ${PUSHGATEWAY_URL}/metrics/job/github_actions
# echo "github_actions_skipped_tests=$(echo ${TEST_RESULTS_STRING} | awk -F'"' '{ print $10 }')" | curl --data-binary @- ${PUSHGATEWAY_URL}/metrics/job/github_actions

# metric{key=value1, key2=value2} value
cat <<EOF | curl --data-binary @- ${PUSHGATEWAY_URL}/metrics/job/github_actions
github_actions_ignored_tests{action_id="${GITHUB_RUN_NUMBER}", commit="${GITHUB_SHA}", actor="${GITHUB_ACTOR}", branch="${GITHUB_REF}"} $(echo ${TEST_RESULTS_STRING} | awk -F'"' '{ print $2 }')
github_actions_total_tests{action_id="${GITHUB_RUN_NUMBER}", commit="${GITHUB_SHA}", actor="${GITHUB_ACTOR}", branch="${GITHUB_REF}"} $(echo ${TEST_RESULTS_STRING} | awk -F'"' '{ print $4 }')
github_actions_passed_tests{action_id="${GITHUB_RUN_NUMBER}", commit="${GITHUB_SHA}", actor="${GITHUB_ACTOR}", branch="${GITHUB_REF}"} $(echo ${TEST_RESULTS_STRING} | awk -F'"' '{ print $6 }')
github_actions_failed_tests{action_id="${GITHUB_RUN_NUMBER}", commit="${GITHUB_SHA}", actor="${GITHUB_ACTOR}", branch="${GITHUB_REF}"} $(echo ${TEST_RESULTS_STRING} | awk -F'"' '{ print $8 }')
github_actions_skipped_tests{action_id="${GITHUB_RUN_NUMBER}", commit="${GITHUB_SHA}", actor="${GITHUB_ACTOR}", branch="${GITHUB_REF}"} $(echo ${TEST_RESULTS_STRING} | awk -F'"' '{ print $10 }')
EOF
