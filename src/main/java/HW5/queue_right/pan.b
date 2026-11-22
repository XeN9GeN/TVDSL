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

		 /* PROC Environment */
;
		;
		
	case 16: // STATE 5
		;
		now.count = trpt->bup.ovals[3];
		now.reject = trpt->bup.ovals[2];
		now.deq = trpt->bup.ovals[1];
		now.enq = trpt->bup.ovals[0];
		;
		ungrab_ints(trpt->bup.ovals, 4);
		goto R999;

	case 17: // STATE 7
		;
		now.enq = trpt->bup.oval;
		;
		goto R999;
;
		;
		
	case 19: // STATE 12
		;
		now.reject = trpt->bup.ovals[2];
		now.deq = trpt->bup.ovals[1];
		now.enq = trpt->bup.ovals[0];
		;
		ungrab_ints(trpt->bup.ovals, 3);
		goto R999;

	case 20: // STATE 14
		;
		now.reject = trpt->bup.oval;
		;
		goto R999;
;
		;
		
	case 22: // STATE 20
		;
		now.count = trpt->bup.ovals[3];
		now.reject = trpt->bup.ovals[2];
		now.deq = trpt->bup.ovals[1];
		now.enq = trpt->bup.ovals[0];
		;
		ungrab_ints(trpt->bup.ovals, 4);
		goto R999;

	case 23: // STATE 22
		;
		now.deq = trpt->bup.oval;
		;
		goto R999;
;
		;
		
	case 25: // STATE 27
		;
		now.reject = trpt->bup.ovals[2];
		now.deq = trpt->bup.ovals[1];
		now.enq = trpt->bup.ovals[0];
		;
		ungrab_ints(trpt->bup.ovals, 3);
		goto R999;

	case 26: // STATE 29
		;
		now.reject = trpt->bup.oval;
		;
		goto R999;

	case 27: // STATE 34
		;
		p_restor(II);
		;
		;
		goto R999;
	}

