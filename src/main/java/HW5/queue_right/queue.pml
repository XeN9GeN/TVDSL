
#define N 2

int count = 0;
bit err = 0;
bit enq = 0;
bit deq = 0;
bit reject = 0;

bit draining = 0;
bit full_reject_done = 0;

active proctype Environment() {
    do
    :: (count < N && !draining) ->
        atomic {
            enq = 1;
            deq = 0;
            reject = 0;
            count = count + 1;

            if
            :: count == N ->
                draining = 1;
                full_reject_done = 0
            :: else -> skip
            fi
        }
        atomic { enq = 0 }

    :: (count == N && draining && !full_reject_done) ->
        atomic {
            enq = 0;
            deq = 0;
            reject = 1;
            full_reject_done = 1
        }
        atomic { reject = 0 }

    :: (count > 0) ->
        atomic {
            enq = 0;
            deq = 1;
            reject = 0;
            count = count - 1;

            if
            :: count == 0 ->
                draining = 0;
                full_reject_done = 0
            :: else -> skip
            fi
        }
        atomic { deq = 0 }

    :: (count == 0) ->
        atomic {
            enq = 0;
            deq = 0;
            reject = 1
        }
        atomic { reject = 0 }
    od
}

ltl S1 { [] (count <= N) }
ltl S2 { [] (count >= 0) }
ltl S3 { [] (!err) }
ltl L1 { [] (enq -> <> (deq || reject)) }
ltl L2 { [] (count > 0 -> <> (count == 0)) }


// G (count <= N)
ltl S1 { [] (count <= N) }

// G (count >= 0)
ltl S2 { [] (count >= 0) }

// G (!err)
ltl S3 { [] (!err) }

// G (enq -> F (deq || reject))
ltl L1 { [] (enq -> <> (deq || reject)) }

// G (count > 0 -> F (count == 0))
ltl L2 { [] (count > 0 -> <> (count == 0)) }

// [] - G ; <> - F