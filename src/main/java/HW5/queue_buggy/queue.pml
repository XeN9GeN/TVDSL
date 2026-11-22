#define N 2

int count = 0;
bit err = 0;
bit enq = 0;
bit deq = 0;
bit reject = 0;

active proctype Environment() {
    do
    :: (count < N) ->
        atomic { enq = 1; deq = 0; reject = 0; count = count + 1 }
        atomic { enq = 0 }
    :: (count == N) ->
        atomic { enq = 0; deq = 0; reject = 1 }
        atomic { reject = 0 }
    :: (count > 0) ->
        atomic { enq = 0; deq = 1; reject = 0; count = count - 1 }
        atomic { deq = 0 }
    :: (count == 0) ->
        atomic { enq = 0; deq = 0; reject = 1 }
        atomic { reject = 0 }
    od
}

active proctype Buggy() {
    do
    :: (count == N) ->
        atomic { err = 1; count = count + 1; }
    od
}

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