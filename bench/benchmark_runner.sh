#!/usr/bin/env bash
set -e

TIME_LIMIT=${TIME_LIMIT:-4000}
OUT_DIR="results"
LOG_DIR="logs"

if [ -z "$PROBLEM_NAME" ]; then
    echo "Error: PROBLEM_NAME is not defined."
    exit 1
fi

if [ ${#INSTANCES[@]} -eq 0 ]; then
    echo "Error: INSTANCES array is empty."
    exit 1
fi

mkdir -p "$OUT_DIR"
mkdir -p "$LOG_DIR"

run_solver(){
  local script_name=$1
  local scala_class=$2
  local instance=$3

  if [ -z "$scala_class" ]; then return; fi

  local log_file="$LOG_DIR/${script_name}_${instance}.log"
  local res_file="$OUT_DIR/${script_name}_${instance}.txt"

  echo "  > Running $script_name on $instance"

  pushd ../.. > /dev/null

  set +e

  sbt --error "runMain $scala_class $DATA_DIR/$instance $TIME_LIMIT" \
      > "bench/$PROBLEM_NAME/$log_file" 2>&1

  local exit_code=$?
  set -e

  popd > /dev/null

  if [ $exit_code -ne 0 ]; then
    echo "    [ERROR] Script $script_name failed for $instance"
    echo " --- Error Log Preview ---"
    tail -n 20 "$log_file"
    exit 1
  else
    grep '^%%' "$log_file" > "$res_file"
    echo "    [SUCCESS] Results saved to $res_file"
  fi
}

echo "========================================"
echo "Starting Benchmark for: $PROBLEM_NAME"
echo "Instances count: ${#INSTANCES[@]}"
echo "========================================"

for inst in "${INSTANCES[@]}"; do
  echo "## Processing $inst" >&2

  run_solver "ddo-scala"   "$CLASS_DDO"   "$inst"
  run_solver "astar-scala" "$CLASS_ASTAR" "$inst"
  run_solver "acs-scala"   "$CLASS_ACS"   "$inst"
  echo ""
done

rm -rf "$LOG_DIR"

echo "All benchmarks completed successfully."