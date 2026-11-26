
#define N 2

int count = 0;
bit err = 0;

bit enq = 0;
bit deq = 0;
bit reject = 0;

bit enq1 = 0;
bit enq2 = 0;
bit deq1 = 0;
bit deq2 = 0;
bit pending1 = 0;
bit pending2 = 0;

//0 — пустая ячейка, 1 и 2 — идентификаторы элементов
byte q[2];

bit draining = 0;
bit full_reject_done = 0;
bit buggy_done = 0;

active proctype Environment() {
    do
    :: (count < N && !draining) ->
        atomic {
            enq = 1;
            deq = 0;
            reject = 0;
            enq1 = 0;
            enq2 = 0;
            deq1 = 0;
            deq2 = 0;

            if
            :: (q[0] != 1 && q[1] != 1) ->
                q[count] = 1;
                enq1 = 1;
                pending1 = 1
            :: (q[0] != 2 && q[1] != 2) ->
                q[count] = 2;
                enq2 = 1;
                pending2 = 1
            fi;

            count = count + 1;

            if
            :: count == N ->
                draining = 1;
                full_reject_done = 0
            :: else -> skip
            fi
        }
        atomic {
            enq = 0;
            enq1 = 0;
            enq2 = 0
        }

    :: (count == N && draining && !full_reject_done) ->
        atomic {
            enq = 0;
            deq = 0;
            reject = 1;
            enq1 = 0;
            enq2 = 0;
            deq1 = 0;
            deq2 = 0;
            full_reject_done = 1
        }
        atomic { reject = 0 }

    :: (count > 0) ->
        atomic {
            enq = 0;
            deq = 1;
            reject = 0;
            enq1 = 0;
            enq2 = 0;
            deq1 = 0;
            deq2 = 0;

            if
            :: q[0] == 1 ->
                    deq1 = 1;
                    pending1 = 0
            :: q[0] == 2 ->
                    deq2 = 1;
                    pending2 = 0
            :: else -> skip
            fi;

            q[0] = q[1];
            q[1] = 0;

            count = count - 1;

            if
            :: count == 0 ->
                draining = 0;
                full_reject_done = 0
            :: else -> skip
            fi
        }
        atomic {
            deq = 0;
            deq1 = 0;
            deq2 = 0
        }

    :: (count == 0) ->
        atomic {
            enq = 0;
            deq = 0;
            reject = 1;
            enq1 = 0;
            enq2 = 0;
            deq1 = 0;
            deq2 = 0
        }
        atomic { reject = 0 }
    od
}

active proctype Buggy() {
    do
    :: (count == N && !buggy_done) ->
        atomic {
            err = 1;
            count = count + 1;
            buggy_done = 1
        }
    od
}

ltl S1 { [] (count <= N) }

ltl S2 { [] (count >= 0) }

ltl S3 { [] (!err) }

ltl L1 {
    [] (
        (pending1 -> <> (!pending1)) &&
        (pending2 -> <> (!pending2))
    )
}

ltl L2 { [] (count > 0 -> <> (count == 0)) }
