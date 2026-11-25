	switch (t->back) {
	default: Uerror("bad return move");
	case  0: goto R999; /* nothing to undo */

		 /* CLAIM L2 */
;
		;
		;
		;
		
	case 5: // STATE 13
		;
		p_restor(II);
		;
		;
		goto R999;

		 /* CLAIM L1 */
;
		;
		;
		;
		
	case 8: // STATE 13
		;
		p_restor(II);
		;
		;
		goto R999;

		 /* CLAIM S3 */
;
		
	case 9: // STATE 1
		goto R999;

	case 10: // STATE 10
		;
		p_restor(II);
		;
		;
		goto R999;

		 /* CLAIM S2 */
;
		
	case 11: // STATE 1
		goto R999;

	case 12: // STATE 10
		;
		p_restor(II);
		;
		;
		goto R999;

		 /* CLAIM S1 */
;
		
	case 13: // STATE 1
		goto R999;

	case 14: // STATE 10
		;
		p_restor(II);
		;
		;
		goto R999;

		 /* CLAIM L2 */
;
		;
		;
		;
		
	case 17: // STATE 13
		;
		p_restor(II);
		;
		;
		goto R999;

		 /* CLAIM L1 */
;
		;
		;
		;
		
	case 20: // STATE 13
		;
		p_restor(II);
		;
		;
		goto R999;

		 /* CLAIM S3 */
;
		
	case 21: // STATE 1
		goto R999;

	case 22: // STATE 10
		;
		p_restor(II);
		;
		;
		goto R999;

		 /* CLAIM S2 */
;
		
	case 23: // STATE 1
		goto R999;

	case 24: // STATE 10
		;
		p_restor(II);
		;
		;
		goto R999;

		 /* CLAIM S1 */
;
		
	case 25: // STATE 1
		goto R999;

	case 26: // STATE 10
		;
		p_restor(II);
		;
		;
		goto R999;

		 /* PROC Environment */
;
		;
		
	case 28: // STATE 5
		;
		now.count = trpt->bup.ovals[3];
		now.reject = trpt->bup.ovals[2];
		now.deq = trpt->bup.ovals[1];
		now.enq = trpt->bup.ovals[0];
		;
		ungrab_ints(trpt->bup.ovals, 4);
		goto R999;

	case 29: // STATE 8
		;
		now.full_reject_done = trpt->bup.ovals[1];
		now.draining = trpt->bup.ovals[0];
		;
		ungrab_ints(trpt->bup.ovals, 2);
		goto R999;
;
		
	case 30: // STATE 12
		goto R999;
;
		
	case 31: // STATE 10
		goto R999;

	case 32: // STATE 14
		;
		now.enq = trpt->bup.oval;
		;
		goto R999;
;
		;
		
	case 34: // STATE 20
		;
		now.full_reject_done = trpt->bup.ovals[3];
		now.reject = trpt->bup.ovals[2];
		now.deq = trpt->bup.ovals[1];
		now.enq = trpt->bup.ovals[0];
		;
		ungrab_ints(trpt->bup.ovals, 4);
		goto R999;

	case 35: // STATE 22
		;
		now.reject = trpt->bup.oval;
		;
		goto R999;
;
		;
		
	case 37: // STATE 28
		;
		now.count = trpt->bup.ovals[3];
		now.reject = trpt->bup.ovals[2];
		now.deq = trpt->bup.ovals[1];
		now.enq = trpt->bup.ovals[0];
		;
		ungrab_ints(trpt->bup.ovals, 4);
		goto R999;

	case 38: // STATE 31
		;
		now.full_reject_done = trpt->bup.ovals[1];
		now.draining = trpt->bup.ovals[0];
		;
		ungrab_ints(trpt->bup.ovals, 2);
		goto R999;
;
		
	case 39: // STATE 35
		goto R999;
;
		
	case 40: // STATE 33
		goto R999;

	case 41: // STATE 37
		;
		now.deq = trpt->bup.oval;
		;
		goto R999;
;
		;
		
	case 43: // STATE 42
		;
		now.reject = trpt->bup.ovals[2];
		now.deq = trpt->bup.ovals[1];
		now.enq = trpt->bup.ovals[0];
		;
		ungrab_ints(trpt->bup.ovals, 3);
		goto R999;

	case 44: // STATE 44
		;
		now.reject = trpt->bup.oval;
		;
		goto R999;

	case 45: // STATE 49
		;
		p_restor(II);
		;
		;
		goto R999;
	}

