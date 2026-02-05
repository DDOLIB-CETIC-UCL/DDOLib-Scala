#!/usr/bin/env bash
set -e

################################################################################
# TSPTW BENCHMARK CONFIGURATION
#
# This script defines the specific configuration (instances, data paths, classes)
# for the TSPTW problem (Traveling Salesman Problem with Time Windows).
#
# It relies on the shared 'benchmark_runner.sh' script to perform the actual
# execution logic (compilation, logging, error handling).
#
# To run: ./run_tsptw.sh
################################################################################

# Problem Configuration
PROBLEM_NAME="tsptw"
DATA_DIR="data/TSPTW/AFG"
TIME_LIMIT=60000

# Main Java Classes Definition
CLASS_DDO="org.ddolibscala.example.bench.TspTwDdo"
CLASS_ASTAR="org.ddolibscala.example.bench.TspTwAstar"
#CLASS_ACS="org.ddolib.examples.bench.TSPTWAcsMain"

# Instances List
INSTANCES=(
  rbg010a.tw
  rbg016a.tw
  rbg016b.tw
  rbg017.2.tw
  rbg017.tw
)

# Load and execute the runner
source ../benchmark_runner.sh