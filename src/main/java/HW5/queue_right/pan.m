#define rand	pan_rand
#define pthread_equal(a,b)	((a)==(b))
#if defined(HAS_CODE) && defined(VERBOSE)
	#ifdef BFS_PAR
		bfs_printf("Pr: %d Tr: %d\n", II, t->forw);
	#else
		cpu_printf("Pr: %d Tr: %d\n", II, t->forw);
	#endif
#endif
	switch (t->forw) {
	default: Uerror("bad forward move");
	case 0:	/* if without executable clauses */
		continue;
	case 1: /* generic 'goto' or 'skip' */
		IfNotBlocked
		_m = 3; goto P999;
	case 2: /* generic 'else' */
		IfNotBlocked
		if (trpt->o_pm&1) continue;
		_m = 3; goto P999;

		 /* CLAIM L2 */
	case 3: // STATE 1 - _spin_nvr.tmp:41 - [((!(!((count>0)))&&!((count==0))))] (0:0:0 - 1)
		
#if defined(VERI) && !defined(NP)
#if NCLAIMS>1
		{	static int reported1 = 0;
			if (verbose && !reported1)
			{	int nn = (int) ((Pclaim *)pptr(0))->_n;
				printf("depth %ld: Claim %s (%d), state %d (line %d)\n",
					depth, procname[spin_c_typ[nn]], nn, (int) ((Pclaim *)pptr(0))->_p, src_claim[ (int) ((Pclaim *)pptr(0))->_p ]);
				reported1 = 1;
				fflush(stdout);
		}	}
#else
		{	static int reported1 = 0;
			if (verbose && !reported1)
			{	printf("depth %d: Claim, state %d (line %d)\n",
					(int) depth, (int) ((Pclaim *)pptr(0))->_p, src_claim[ (int) ((Pclaim *)pptr(0))->_p ]);
				reported1 = 1;
				fflush(stdout);
		}	}
#endif
#endif
		reached[5][1] = 1;
		if (!(( !( !((now.count>0)))&& !((now.count==0)))))
			continue;
		_m = 3; goto P999; /* 0 */
	case 4: // STATE 8 - _spin_nvr.tmp:46 - [(!((count==0)))] (0:0:0 - 1)
		
#if defined(VERI) && !defined(NP)
#if NCLAIMS>1
		{	static int reported8 = 0;
			if (verbose && !reported8)
			{	int nn = (int) ((Pclaim *)pptr(0))->_n;
				printf("depth %ld: Claim %s (%d), state %d (line %d)\n",
					depth, procname[spin_c_typ[nn]], nn, (int) ((Pclaim *)pptr(0))->_p, src_claim[ (int) ((Pclaim *)pptr(0))->_p ]);
				reported8 = 1;
				fflush(stdout);
		}	}
#else
		{	static int reported8 = 0;
			if (verbose && !reported8)
			{	printf("depth %d: Claim, state %d (line %d)\n",
					(int) depth, (int) ((Pclaim *)pptr(0))->_p, src_claim[ (int) ((Pclaim *)pptr(0))->_p ]);
				reported8 = 1;
				fflush(stdout);
		}	}
#endif
#endif
		reached[5][8] = 1;
		if (!( !((now.count==0))))
			continue;
		_m = 3; goto P999; /* 0 */
	case 5: // STATE 13 - _spin_nvr.tmp:48 - [-end-] (0:0:0 - 1)
		
#if defined(VERI) && !defined(NP)
#if NCLAIMS>1
		{	static int reported13 = 0;
			if (verbose && !reported13)
			{	int nn = (int) ((Pclaim *)pptr(0))->_n;
				printf("depth %ld: Claim %s (%d), state %d (line %d)\n",
					depth, procname[spin_c_typ[nn]], nn, (int) ((Pclaim *)pptr(0))->_p, src_claim[ (int) ((Pclaim *)pptr(0))->_p ]);
				reported13 = 1;
				fflush(stdout);
		}	}
#else
		{	static int reported13 = 0;
			if (verbose && !reported13)
			{	printf("depth %d: Claim, state %d (line %d)\n",
					(int) depth, (int) ((Pclaim *)pptr(0))->_p, src_claim[ (int) ((Pclaim *)pptr(0))->_p ]);
				reported13 = 1;
				fflush(stdout);
		}	}
#endif
#endif
		reached[5][13] = 1;
		if (!delproc(1, II)) continue;
		_m = 3; goto P999; /* 0 */

		 /* CLAIM L1 */
	case 6: // STATE 1 - _spin_nvr.tmp:30 - [((!(!(enq))&&!((deq||reject))))] (0:0:0 - 1)
		
#if defined(VERI) && !defined(NP)
#if NCLAIMS>1
		{	static int reported1 = 0;
			if (verbose && !reported1)
			{	int nn = (int) ((Pclaim *)pptr(0))->_n;
				printf("depth %ld: Claim %s (%d), state %d (line %d)\n",
					depth, procname[spin_c_typ[nn]], nn, (int) ((Pclaim *)pptr(0))->_p, src_claim[ (int) ((Pclaim *)pptr(0))->_p ]);
				reported1 = 1;
				fflush(stdout);
		}	}
#else
		{	static int reported1 = 0;
			if (verbose && !reported1)
			{	printf("depth %d: Claim, state %d (line %d)\n",
					(int) depth, (int) ((Pclaim *)pptr(0))->_p, src_claim[ (int) ((Pclaim *)pptr(0))->_p ]);
				reported1 = 1;
				fflush(stdout);
		}	}
#endif
#endif
		reached[4][1] = 1;
		if (!(( !( !(((int)now.enq)))&& !((((int)now.deq)||((int)now.reject))))))
			continue;
		_m = 3; goto P999; /* 0 */
	case 7: // STATE 8 - _spin_nvr.tmp:35 - [(!((deq||reject)))] (0:0:0 - 1)
		
#if defined(VERI) && !defined(NP)
#if NCLAIMS>1
		{	static int reported8 = 0;
			if (verbose && !reported8)
			{	int nn = (int) ((Pclaim *)pptr(0))->_n;
				printf("depth %ld: Claim %s (%d), state %d (line %d)\n",
					depth, procname[spin_c_typ[nn]], nn, (int) ((Pclaim *)pptr(0))->_p, src_claim[ (int) ((Pclaim *)pptr(0))->_p ]);
				reported8 = 1;
				fflush(stdout);
		}	}
#else
		{	static int reported8 = 0;
			if (verbose && !reported8)
			{	printf("depth %d: Claim, state %d (line %d)\n",
					(int) depth, (int) ((Pclaim *)pptr(0))->_p, src_claim[ (int) ((Pclaim *)pptr(0))->_p ]);
				reported8 = 1;
				fflush(stdout);
		}	}
#endif
#endif
		reached[4][8] = 1;
		if (!( !((((int)now.deq)||((int)now.reject)))))
			continue;
		_m = 3; goto P999; /* 0 */
	case 8: // STATE 13 - _spin_nvr.tmp:37 - [-end-] (0:0:0 - 1)
		
#if defined(VERI) && !defined(NP)
#if NCLAIMS>1
		{	static int reported13 = 0;
			if (verbose && !reported13)
			{	int nn = (int) ((Pclaim *)pptr(0))->_n;
				printf("depth %ld: Claim %s (%d), state %d (line %d)\n",
					depth, procname[spin_c_typ[nn]], nn, (int) ((Pclaim *)pptr(0))->_p, src_claim[ (int) ((Pclaim *)pptr(0))->_p ]);
				reported13 = 1;
				fflush(stdout);
		}	}
#else
		{	static int reported13 = 0;
			if (verbose && !reported13)
			{	printf("depth %d: Claim, state %d (line %d)\n",
					(int) depth, (int) ((Pclaim *)pptr(0))->_p, src_claim[ (int) ((Pclaim *)pptr(0))->_p ]);
				reported13 = 1;
				fflush(stdout);
		}	}
#endif
#endif
		reached[4][13] = 1;
		if (!delproc(1, II)) continue;
		_m = 3; goto P999; /* 0 */

		 /* CLAIM S3 */
	case 9: // STATE 1 - _spin_nvr.tmp:21 - [(!(!(err)))] (6:0:0 - 1)
		
#if defined(VERI) && !defined(NP)
#if NCLAIMS>1
		{	static int reported1 = 0;
			if (verbose && !reported1)
			{	int nn = (int) ((Pclaim *)pptr(0))->_n;
				printf("depth %ld: Claim %s (%d), state %d (line %d)\n",
					depth, procname[spin_c_typ[nn]], nn, (int) ((Pclaim *)pptr(0))->_p, src_claim[ (int) ((Pclaim *)pptr(0))->_p ]);
				reported1 = 1;
				fflush(stdout);
		}	}
#else
		{	static int reported1 = 0;
			if (verbose && !reported1)
			{	printf("depth %d: Claim, state %d (line %d)\n",
					(int) depth, (int) ((Pclaim *)pptr(0))->_p, src_claim[ (int) ((Pclaim *)pptr(0))->_p ]);
				reported1 = 1;
				fflush(stdout);
		}	}
#endif
#endif
		reached[3][1] = 1;
		if (!( !( !(((int)now.err)))))
			continue;
		/* merge: assert(!(!(!(err))))(0, 2, 6) */
		reached[3][2] = 1;
		spin_assert( !( !( !(((int)now.err)))), " !( !( !(err)))", II, tt, t);
		/* merge: .(goto)(0, 7, 6) */
		reached[3][7] = 1;
		;
		_m = 3; goto P999; /* 2 */
	case 10: // STATE 10 - _spin_nvr.tmp:26 - [-end-] (0:0:0 - 1)
		
#if defined(VERI) && !defined(NP)
#if NCLAIMS>1
		{	static int reported10 = 0;
			if (verbose && !reported10)
			{	int nn = (int) ((Pclaim *)pptr(0))->_n;
				printf("depth %ld: Claim %s (%d), state %d (line %d)\n",
					depth, procname[spin_c_typ[nn]], nn, (int) ((Pclaim *)pptr(0))->_p, src_claim[ (int) ((Pclaim *)pptr(0))->_p ]);
				reported10 = 1;
				fflush(stdout);
		}	}
#else
		{	static int reported10 = 0;
			if (verbose && !reported10)
			{	printf("depth %d: Claim, state %d (line %d)\n",
					(int) depth, (int) ((Pclaim *)pptr(0))->_p, src_claim[ (int) ((Pclaim *)pptr(0))->_p ]);
				reported10 = 1;
				fflush(stdout);
		}	}
#endif
#endif
		reached[3][10] = 1;
		if (!delproc(1, II)) continue;
		_m = 3; goto P999; /* 0 */

		 /* CLAIM S2 */
	case 11: // STATE 1 - _spin_nvr.tmp:12 - [(!((count>=0)))] (6:0:0 - 1)
		
#if defined(VERI) && !defined(NP)
#if NCLAIMS>1
		{	static int reported1 = 0;
			if (verbose && !reported1)
			{	int nn = (int) ((Pclaim *)pptr(0))->_n;
				printf("depth %ld: Claim %s (%d), state %d (line %d)\n",
					depth, procname[spin_c_typ[nn]], nn, (int) ((Pclaim *)pptr(0))->_p, src_claim[ (int) ((Pclaim *)pptr(0))->_p ]);
				reported1 = 1;
				fflush(stdout);
		}	}
#else
		{	static int reported1 = 0;
			if (verbose && !reported1)
			{	printf("depth %d: Claim, state %d (line %d)\n",
					(int) depth, (int) ((Pclaim *)pptr(0))->_p, src_claim[ (int) ((Pclaim *)pptr(0))->_p ]);
				reported1 = 1;
				fflush(stdout);
		}	}
#endif
#endif
		reached[2][1] = 1;
		if (!( !((now.count>=0))))
			continue;
		/* merge: assert(!(!((count>=0))))(0, 2, 6) */
		reached[2][2] = 1;
		spin_assert( !( !((now.count>=0))), " !( !((count>=0)))", II, tt, t);
		/* merge: .(goto)(0, 7, 6) */
		reached[2][7] = 1;
		;
		_m = 3; goto P999; /* 2 */
	case 12: // STATE 10 - _spin_nvr.tmp:17 - [-end-] (0:0:0 - 1)
		
#if defined(VERI) && !defined(NP)
#if NCLAIMS>1
		{	static int reported10 = 0;
			if (verbose && !reported10)
			{	int nn = (int) ((Pclaim *)pptr(0))->_n;
				printf("depth %ld: Claim %s (%d), state %d (line %d)\n",
					depth, procname[spin_c_typ[nn]], nn, (int) ((Pclaim *)pptr(0))->_p, src_claim[ (int) ((Pclaim *)pptr(0))->_p ]);
				reported10 = 1;
				fflush(stdout);
		}	}
#else
		{	static int reported10 = 0;
			if (verbose && !reported10)
			{	printf("depth %d: Claim, state %d (line %d)\n",
					(int) depth, (int) ((Pclaim *)pptr(0))->_p, src_claim[ (int) ((Pclaim *)pptr(0))->_p ]);
				reported10 = 1;
				fflush(stdout);
		}	}
#endif
#endif
		reached[2][10] = 1;
		if (!delproc(1, II)) continue;
		_m = 3; goto P999; /* 0 */

		 /* CLAIM S1 */
	case 13: // STATE 1 - _spin_nvr.tmp:3 - [(!((count<=2)))] (6:0:0 - 1)
		
#if defined(VERI) && !defined(NP)
#if NCLAIMS>1
		{	static int reported1 = 0;
			if (verbose && !reported1)
			{	int nn = (int) ((Pclaim *)pptr(0))->_n;
				printf("depth %ld: Claim %s (%d), state %d (line %d)\n",
					depth, procname[spin_c_typ[nn]], nn, (int) ((Pclaim *)pptr(0))->_p, src_claim[ (int) ((Pclaim *)pptr(0))->_p ]);
				reported1 = 1;
				fflush(stdout);
		}	}
#else
		{	static int reported1 = 0;
			if (verbose && !reported1)
			{	printf("depth %d: Claim, state %d (line %d)\n",
					(int) depth, (int) ((Pclaim *)pptr(0))->_p, src_claim[ (int) ((Pclaim *)pptr(0))->_p ]);
				reported1 = 1;
				fflush(stdout);
		}	}
#endif
#endif
		reached[1][1] = 1;
		if (!( !((now.count<=2))))
			continue;
		/* merge: assert(!(!((count<=2))))(0, 2, 6) */
		reached[1][2] = 1;
		spin_assert( !( !((now.count<=2))), " !( !((count<=2)))", II, tt, t);
		/* merge: .(goto)(0, 7, 6) */
		reached[1][7] = 1;
		;
		_m = 3; goto P999; /* 2 */
	case 14: // STATE 10 - _spin_nvr.tmp:8 - [-end-] (0:0:0 - 1)
		
#if defined(VERI) && !defined(NP)
#if NCLAIMS>1
		{	static int reported10 = 0;
			if (verbose && !reported10)
			{	int nn = (int) ((Pclaim *)pptr(0))->_n;
				printf("depth %ld: Claim %s (%d), state %d (line %d)\n",
					depth, procname[spin_c_typ[nn]], nn, (int) ((Pclaim *)pptr(0))->_p, src_claim[ (int) ((Pclaim *)pptr(0))->_p ]);
				reported10 = 1;
				fflush(stdout);
		}	}
#else
		{	static int reported10 = 0;
			if (verbose && !reported10)
			{	printf("depth %d: Claim, state %d (line %d)\n",
					(int) depth, (int) ((Pclaim *)pptr(0))->_p, src_claim[ (int) ((Pclaim *)pptr(0))->_p ]);
				reported10 = 1;
				fflush(stdout);
		}	}
#endif
#endif
		reached[1][10] = 1;
		if (!delproc(1, II)) continue;
		_m = 3; goto P999; /* 0 */

		 /* PROC Environment */
	case 15: // STATE 1 - queue.pml:11 - [((count<2))] (0:0:0 - 1)
		IfNotBlocked
		reached[0][1] = 1;
		if (!((now.count<2)))
			continue;
		_m = 3; goto P999; /* 0 */
	case 16: // STATE 2 - queue.pml:12 - [enq = 1] (0:8:4 - 1)
		IfNotBlocked
		reached[0][2] = 1;
		(trpt+1)->bup.ovals = grab_ints(4);
		(trpt+1)->bup.ovals[0] = ((int)now.enq);
		now.enq = 1;
#ifdef VAR_RANGES
		logval("enq", ((int)now.enq));
#endif
		;
		/* merge: deq = 0(8, 3, 8) */
		reached[0][3] = 1;
		(trpt+1)->bup.ovals[1] = ((int)now.deq);
		now.deq = 0;
#ifdef VAR_RANGES
		logval("deq", ((int)now.deq));
#endif
		;
		/* merge: reject = 0(8, 4, 8) */
		reached[0][4] = 1;
		(trpt+1)->bup.ovals[2] = ((int)now.reject);
		now.reject = 0;
#ifdef VAR_RANGES
		logval("reject", ((int)now.reject));
#endif
		;
		/* merge: count = (count+1)(8, 5, 8) */
		reached[0][5] = 1;
		(trpt+1)->bup.ovals[3] = now.count;
		now.count = (now.count+1);
#ifdef VAR_RANGES
		logval("count", now.count);
#endif
		;
		_m = 3; goto P999; /* 3 */
	case 17: // STATE 7 - queue.pml:13 - [enq = 0] (0:0:1 - 1)
		IfNotBlocked
		reached[0][7] = 1;
		(trpt+1)->bup.oval = ((int)now.enq);
		now.enq = 0;
#ifdef VAR_RANGES
		logval("enq", ((int)now.enq));
#endif
		;
		_m = 3; goto P999; /* 0 */
	case 18: // STATE 9 - queue.pml:14 - [((count==2))] (0:0:0 - 1)
		IfNotBlocked
		reached[0][9] = 1;
		if (!((now.count==2)))
			continue;
		_m = 3; goto P999; /* 0 */
	case 19: // STATE 10 - queue.pml:15 - [enq = 0] (0:15:3 - 1)
		IfNotBlocked
		reached[0][10] = 1;
		(trpt+1)->bup.ovals = grab_ints(3);
		(trpt+1)->bup.ovals[0] = ((int)now.enq);
		now.enq = 0;
#ifdef VAR_RANGES
		logval("enq", ((int)now.enq));
#endif
		;
		/* merge: deq = 0(15, 11, 15) */
		reached[0][11] = 1;
		(trpt+1)->bup.ovals[1] = ((int)now.deq);
		now.deq = 0;
#ifdef VAR_RANGES
		logval("deq", ((int)now.deq));
#endif
		;
		/* merge: reject = 1(15, 12, 15) */
		reached[0][12] = 1;
		(trpt+1)->bup.ovals[2] = ((int)now.reject);
		now.reject = 1;
#ifdef VAR_RANGES
		logval("reject", ((int)now.reject));
#endif
		;
		_m = 3; goto P999; /* 2 */
	case 20: // STATE 14 - queue.pml:16 - [reject = 0] (0:0:1 - 1)
		IfNotBlocked
		reached[0][14] = 1;
		(trpt+1)->bup.oval = ((int)now.reject);
		now.reject = 0;
#ifdef VAR_RANGES
		logval("reject", ((int)now.reject));
#endif
		;
		_m = 3; goto P999; /* 0 */
	case 21: // STATE 16 - queue.pml:17 - [((count>0))] (0:0:0 - 1)
		IfNotBlocked
		reached[0][16] = 1;
		if (!((now.count>0)))
			continue;
		_m = 3; goto P999; /* 0 */
	case 22: // STATE 17 - queue.pml:18 - [enq = 0] (0:23:4 - 1)
		IfNotBlocked
		reached[0][17] = 1;
		(trpt+1)->bup.ovals = grab_ints(4);
		(trpt+1)->bup.ovals[0] = ((int)now.enq);
		now.enq = 0;
#ifdef VAR_RANGES
		logval("enq", ((int)now.enq));
#endif
		;
		/* merge: deq = 1(23, 18, 23) */
		reached[0][18] = 1;
		(trpt+1)->bup.ovals[1] = ((int)now.deq);
		now.deq = 1;
#ifdef VAR_RANGES
		logval("deq", ((int)now.deq));
#endif
		;
		/* merge: reject = 0(23, 19, 23) */
		reached[0][19] = 1;
		(trpt+1)->bup.ovals[2] = ((int)now.reject);
		now.reject = 0;
#ifdef VAR_RANGES
		logval("reject", ((int)now.reject));
#endif
		;
		/* merge: count = (count-1)(23, 20, 23) */
		reached[0][20] = 1;
		(trpt+1)->bup.ovals[3] = now.count;
		now.count = (now.count-1);
#ifdef VAR_RANGES
		logval("count", now.count);
#endif
		;
		_m = 3; goto P999; /* 3 */
	case 23: // STATE 22 - queue.pml:19 - [deq = 0] (0:0:1 - 1)
		IfNotBlocked
		reached[0][22] = 1;
		(trpt+1)->bup.oval = ((int)now.deq);
		now.deq = 0;
#ifdef VAR_RANGES
		logval("deq", ((int)now.deq));
#endif
		;
		_m = 3; goto P999; /* 0 */
	case 24: // STATE 24 - queue.pml:20 - [((count==0))] (0:0:0 - 1)
		IfNotBlocked
		reached[0][24] = 1;
		if (!((now.count==0)))
			continue;
		_m = 3; goto P999; /* 0 */
	case 25: // STATE 25 - queue.pml:21 - [enq = 0] (0:30:3 - 1)
		IfNotBlocked
		reached[0][25] = 1;
		(trpt+1)->bup.ovals = grab_ints(3);
		(trpt+1)->bup.ovals[0] = ((int)now.enq);
		now.enq = 0;
#ifdef VAR_RANGES
		logval("enq", ((int)now.enq));
#endif
		;
		/* merge: deq = 0(30, 26, 30) */
		reached[0][26] = 1;
		(trpt+1)->bup.ovals[1] = ((int)now.deq);
		now.deq = 0;
#ifdef VAR_RANGES
		logval("deq", ((int)now.deq));
#endif
		;
		/* merge: reject = 1(30, 27, 30) */
		reached[0][27] = 1;
		(trpt+1)->bup.ovals[2] = ((int)now.reject);
		now.reject = 1;
#ifdef VAR_RANGES
		logval("reject", ((int)now.reject));
#endif
		;
		_m = 3; goto P999; /* 2 */
	case 26: // STATE 29 - queue.pml:22 - [reject = 0] (0:0:1 - 1)
		IfNotBlocked
		reached[0][29] = 1;
		(trpt+1)->bup.oval = ((int)now.reject);
		now.reject = 0;
#ifdef VAR_RANGES
		logval("reject", ((int)now.reject));
#endif
		;
		_m = 3; goto P999; /* 0 */
	case 27: // STATE 34 - queue.pml:24 - [-end-] (0:0:0 - 1)
		IfNotBlocked
		reached[0][34] = 1;
		if (!delproc(1, II)) continue;
		_m = 3; goto P999; /* 0 */
	case  _T5:	/* np_ */
		if (!((!(trpt->o_pm&4) && !(trpt->tau&128))))
			continue;
		/* else fall through */
	case  _T2:	/* true */
		_m = 3; goto P999;
#undef rand
	}

