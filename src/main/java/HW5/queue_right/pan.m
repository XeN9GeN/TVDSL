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
	case 3: // STATE 1 - _spin_nvr.tmp:90 - [((!(!((count>0)))&&!((count==0))))] (0:0:0 - 1)
		
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
		reached[10][1] = 1;
		if (!(( !( !((now.count>0)))&& !((now.count==0)))))
			continue;
		_m = 3; goto P999; /* 0 */
	case 4: // STATE 8 - _spin_nvr.tmp:95 - [(!((count==0)))] (0:0:0 - 1)
		
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
		reached[10][8] = 1;
		if (!( !((now.count==0))))
			continue;
		_m = 3; goto P999; /* 0 */
	case 5: // STATE 13 - _spin_nvr.tmp:97 - [-end-] (0:0:0 - 1)
		
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
		reached[10][13] = 1;
		if (!delproc(1, II)) continue;
		_m = 3; goto P999; /* 0 */

		 /* CLAIM L1 */
	case 6: // STATE 1 - _spin_nvr.tmp:79 - [((!(!(enq))&&!((deq||reject))))] (0:0:0 - 1)
		
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
		reached[9][1] = 1;
		if (!(( !( !(((int)now.enq)))&& !((((int)now.deq)||((int)now.reject))))))
			continue;
		_m = 3; goto P999; /* 0 */
	case 7: // STATE 8 - _spin_nvr.tmp:84 - [(!((deq||reject)))] (0:0:0 - 1)
		
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
		reached[9][8] = 1;
		if (!( !((((int)now.deq)||((int)now.reject)))))
			continue;
		_m = 3; goto P999; /* 0 */
	case 8: // STATE 13 - _spin_nvr.tmp:86 - [-end-] (0:0:0 - 1)
		
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
		reached[9][13] = 1;
		if (!delproc(1, II)) continue;
		_m = 3; goto P999; /* 0 */

		 /* CLAIM S3 */
	case 9: // STATE 1 - _spin_nvr.tmp:70 - [(!(!(err)))] (6:0:0 - 1)
		
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
		reached[8][1] = 1;
		if (!( !( !(((int)now.err)))))
			continue;
		/* merge: assert(!(!(!(err))))(0, 2, 6) */
		reached[8][2] = 1;
		spin_assert( !( !( !(((int)now.err)))), " !( !( !(err)))", II, tt, t);
		/* merge: .(goto)(0, 7, 6) */
		reached[8][7] = 1;
		;
		_m = 3; goto P999; /* 2 */
	case 10: // STATE 10 - _spin_nvr.tmp:75 - [-end-] (0:0:0 - 1)
		
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
		reached[8][10] = 1;
		if (!delproc(1, II)) continue;
		_m = 3; goto P999; /* 0 */

		 /* CLAIM S2 */
	case 11: // STATE 1 - _spin_nvr.tmp:61 - [(!((count>=0)))] (6:0:0 - 1)
		
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
		reached[7][1] = 1;
		if (!( !((now.count>=0))))
			continue;
		/* merge: assert(!(!((count>=0))))(0, 2, 6) */
		reached[7][2] = 1;
		spin_assert( !( !((now.count>=0))), " !( !((count>=0)))", II, tt, t);
		/* merge: .(goto)(0, 7, 6) */
		reached[7][7] = 1;
		;
		_m = 3; goto P999; /* 2 */
	case 12: // STATE 10 - _spin_nvr.tmp:66 - [-end-] (0:0:0 - 1)
		
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
		reached[7][10] = 1;
		if (!delproc(1, II)) continue;
		_m = 3; goto P999; /* 0 */

		 /* CLAIM S1 */
	case 13: // STATE 1 - _spin_nvr.tmp:52 - [(!((count<=2)))] (6:0:0 - 1)
		
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
		reached[6][1] = 1;
		if (!( !((now.count<=2))))
			continue;
		/* merge: assert(!(!((count<=2))))(0, 2, 6) */
		reached[6][2] = 1;
		spin_assert( !( !((now.count<=2))), " !( !((count<=2)))", II, tt, t);
		/* merge: .(goto)(0, 7, 6) */
		reached[6][7] = 1;
		;
		_m = 3; goto P999; /* 2 */
	case 14: // STATE 10 - _spin_nvr.tmp:57 - [-end-] (0:0:0 - 1)
		
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
		reached[6][10] = 1;
		if (!delproc(1, II)) continue;
		_m = 3; goto P999; /* 0 */

		 /* CLAIM L2 */
	case 15: // STATE 1 - _spin_nvr.tmp:41 - [((!(!((count>0)))&&!((count==0))))] (0:0:0 - 1)
		
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
	case 16: // STATE 8 - _spin_nvr.tmp:46 - [(!((count==0)))] (0:0:0 - 1)
		
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
	case 17: // STATE 13 - _spin_nvr.tmp:48 - [-end-] (0:0:0 - 1)
		
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
	case 18: // STATE 1 - _spin_nvr.tmp:30 - [((!(!(enq))&&!((deq||reject))))] (0:0:0 - 1)
		
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
	case 19: // STATE 8 - _spin_nvr.tmp:35 - [(!((deq||reject)))] (0:0:0 - 1)
		
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
	case 20: // STATE 13 - _spin_nvr.tmp:37 - [-end-] (0:0:0 - 1)
		
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
	case 21: // STATE 1 - _spin_nvr.tmp:21 - [(!(!(err)))] (6:0:0 - 1)
		
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
	case 22: // STATE 10 - _spin_nvr.tmp:26 - [-end-] (0:0:0 - 1)
		
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
	case 23: // STATE 1 - _spin_nvr.tmp:12 - [(!((count>=0)))] (6:0:0 - 1)
		
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
	case 24: // STATE 10 - _spin_nvr.tmp:17 - [-end-] (0:0:0 - 1)
		
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
	case 25: // STATE 1 - _spin_nvr.tmp:3 - [(!((count<=2)))] (6:0:0 - 1)
		
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
	case 26: // STATE 10 - _spin_nvr.tmp:8 - [-end-] (0:0:0 - 1)
		
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
	case 27: // STATE 1 - queue.pml:15 - [(((count<2)&&!(draining)))] (0:0:0 - 1)
		IfNotBlocked
		reached[0][1] = 1;
		if (!(((now.count<2)&& !(((int)now.draining)))))
			continue;
		_m = 3; goto P999; /* 0 */
	case 28: // STATE 2 - queue.pml:17 - [enq = 1] (0:11:4 - 1)
		IfNotBlocked
		reached[0][2] = 1;
		(trpt+1)->bup.ovals = grab_ints(4);
		(trpt+1)->bup.ovals[0] = ((int)now.enq);
		now.enq = 1;
#ifdef VAR_RANGES
		logval("enq", ((int)now.enq));
#endif
		;
		/* merge: deq = 0(11, 3, 11) */
		reached[0][3] = 1;
		(trpt+1)->bup.ovals[1] = ((int)now.deq);
		now.deq = 0;
#ifdef VAR_RANGES
		logval("deq", ((int)now.deq));
#endif
		;
		/* merge: reject = 0(11, 4, 11) */
		reached[0][4] = 1;
		(trpt+1)->bup.ovals[2] = ((int)now.reject);
		now.reject = 0;
#ifdef VAR_RANGES
		logval("reject", ((int)now.reject));
#endif
		;
		/* merge: count = (count+1)(11, 5, 11) */
		reached[0][5] = 1;
		(trpt+1)->bup.ovals[3] = now.count;
		now.count = (now.count+1);
#ifdef VAR_RANGES
		logval("count", now.count);
#endif
		;
		_m = 3; goto P999; /* 3 */
	case 29: // STATE 6 - queue.pml:23 - [((count==2))] (15:0:2 - 1)
		IfNotBlocked
		reached[0][6] = 1;
		if (!((now.count==2)))
			continue;
		/* merge: draining = 1(15, 7, 15) */
		reached[0][7] = 1;
		(trpt+1)->bup.ovals = grab_ints(2);
		(trpt+1)->bup.ovals[0] = ((int)now.draining);
		now.draining = 1;
#ifdef VAR_RANGES
		logval("draining", ((int)now.draining));
#endif
		;
		/* merge: full_reject_done = 0(15, 8, 15) */
		reached[0][8] = 1;
		(trpt+1)->bup.ovals[1] = ((int)now.full_reject_done);
		now.full_reject_done = 0;
#ifdef VAR_RANGES
		logval("full_reject_done", ((int)now.full_reject_done));
#endif
		;
		/* merge: .(goto)(15, 12, 15) */
		reached[0][12] = 1;
		;
		_m = 3; goto P999; /* 3 */
	case 30: // STATE 12 - queue.pml:28 - [.(goto)] (0:15:0 - 2)
		IfNotBlocked
		reached[0][12] = 1;
		;
		_m = 3; goto P999; /* 0 */
	case 31: // STATE 10 - queue.pml:26 - [(1)] (15:0:0 - 1)
		IfNotBlocked
		reached[0][10] = 1;
		if (!(1))
			continue;
		/* merge: .(goto)(15, 12, 15) */
		reached[0][12] = 1;
		;
		_m = 3; goto P999; /* 1 */
	case 32: // STATE 14 - queue.pml:29 - [enq = 0] (0:0:1 - 1)
		IfNotBlocked
		reached[0][14] = 1;
		(trpt+1)->bup.oval = ((int)now.enq);
		now.enq = 0;
#ifdef VAR_RANGES
		logval("enq", ((int)now.enq));
#endif
		;
		_m = 3; goto P999; /* 0 */
	case 33: // STATE 16 - queue.pml:31 - [((((count==2)&&draining)&&!(full_reject_done)))] (0:0:0 - 1)
		IfNotBlocked
		reached[0][16] = 1;
		if (!((((now.count==2)&&((int)now.draining))&& !(((int)now.full_reject_done)))))
			continue;
		_m = 3; goto P999; /* 0 */
	case 34: // STATE 17 - queue.pml:33 - [enq = 0] (0:23:4 - 1)
		IfNotBlocked
		reached[0][17] = 1;
		(trpt+1)->bup.ovals = grab_ints(4);
		(trpt+1)->bup.ovals[0] = ((int)now.enq);
		now.enq = 0;
#ifdef VAR_RANGES
		logval("enq", ((int)now.enq));
#endif
		;
		/* merge: deq = 0(23, 18, 23) */
		reached[0][18] = 1;
		(trpt+1)->bup.ovals[1] = ((int)now.deq);
		now.deq = 0;
#ifdef VAR_RANGES
		logval("deq", ((int)now.deq));
#endif
		;
		/* merge: reject = 1(23, 19, 23) */
		reached[0][19] = 1;
		(trpt+1)->bup.ovals[2] = ((int)now.reject);
		now.reject = 1;
#ifdef VAR_RANGES
		logval("reject", ((int)now.reject));
#endif
		;
		/* merge: full_reject_done = 1(23, 20, 23) */
		reached[0][20] = 1;
		(trpt+1)->bup.ovals[3] = ((int)now.full_reject_done);
		now.full_reject_done = 1;
#ifdef VAR_RANGES
		logval("full_reject_done", ((int)now.full_reject_done));
#endif
		;
		_m = 3; goto P999; /* 3 */
	case 35: // STATE 22 - queue.pml:38 - [reject = 0] (0:0:1 - 1)
		IfNotBlocked
		reached[0][22] = 1;
		(trpt+1)->bup.oval = ((int)now.reject);
		now.reject = 0;
#ifdef VAR_RANGES
		logval("reject", ((int)now.reject));
#endif
		;
		_m = 3; goto P999; /* 0 */
	case 36: // STATE 24 - queue.pml:40 - [((count>0))] (0:0:0 - 1)
		IfNotBlocked
		reached[0][24] = 1;
		if (!((now.count>0)))
			continue;
		_m = 3; goto P999; /* 0 */
	case 37: // STATE 25 - queue.pml:42 - [enq = 0] (0:34:4 - 1)
		IfNotBlocked
		reached[0][25] = 1;
		(trpt+1)->bup.ovals = grab_ints(4);
		(trpt+1)->bup.ovals[0] = ((int)now.enq);
		now.enq = 0;
#ifdef VAR_RANGES
		logval("enq", ((int)now.enq));
#endif
		;
		/* merge: deq = 1(34, 26, 34) */
		reached[0][26] = 1;
		(trpt+1)->bup.ovals[1] = ((int)now.deq);
		now.deq = 1;
#ifdef VAR_RANGES
		logval("deq", ((int)now.deq));
#endif
		;
		/* merge: reject = 0(34, 27, 34) */
		reached[0][27] = 1;
		(trpt+1)->bup.ovals[2] = ((int)now.reject);
		now.reject = 0;
#ifdef VAR_RANGES
		logval("reject", ((int)now.reject));
#endif
		;
		/* merge: count = (count-1)(34, 28, 34) */
		reached[0][28] = 1;
		(trpt+1)->bup.ovals[3] = now.count;
		now.count = (now.count-1);
#ifdef VAR_RANGES
		logval("count", now.count);
#endif
		;
		_m = 3; goto P999; /* 3 */
	case 38: // STATE 29 - queue.pml:48 - [((count==0))] (38:0:2 - 1)
		IfNotBlocked
		reached[0][29] = 1;
		if (!((now.count==0)))
			continue;
		/* merge: draining = 0(38, 30, 38) */
		reached[0][30] = 1;
		(trpt+1)->bup.ovals = grab_ints(2);
		(trpt+1)->bup.ovals[0] = ((int)now.draining);
		now.draining = 0;
#ifdef VAR_RANGES
		logval("draining", ((int)now.draining));
#endif
		;
		/* merge: full_reject_done = 0(38, 31, 38) */
		reached[0][31] = 1;
		(trpt+1)->bup.ovals[1] = ((int)now.full_reject_done);
		now.full_reject_done = 0;
#ifdef VAR_RANGES
		logval("full_reject_done", ((int)now.full_reject_done));
#endif
		;
		/* merge: .(goto)(38, 35, 38) */
		reached[0][35] = 1;
		;
		_m = 3; goto P999; /* 3 */
	case 39: // STATE 35 - queue.pml:53 - [.(goto)] (0:38:0 - 2)
		IfNotBlocked
		reached[0][35] = 1;
		;
		_m = 3; goto P999; /* 0 */
	case 40: // STATE 33 - queue.pml:51 - [(1)] (38:0:0 - 1)
		IfNotBlocked
		reached[0][33] = 1;
		if (!(1))
			continue;
		/* merge: .(goto)(38, 35, 38) */
		reached[0][35] = 1;
		;
		_m = 3; goto P999; /* 1 */
	case 41: // STATE 37 - queue.pml:54 - [deq = 0] (0:0:1 - 1)
		IfNotBlocked
		reached[0][37] = 1;
		(trpt+1)->bup.oval = ((int)now.deq);
		now.deq = 0;
#ifdef VAR_RANGES
		logval("deq", ((int)now.deq));
#endif
		;
		_m = 3; goto P999; /* 0 */
	case 42: // STATE 39 - queue.pml:56 - [((count==0))] (0:0:0 - 1)
		IfNotBlocked
		reached[0][39] = 1;
		if (!((now.count==0)))
			continue;
		_m = 3; goto P999; /* 0 */
	case 43: // STATE 40 - queue.pml:58 - [enq = 0] (0:45:3 - 1)
		IfNotBlocked
		reached[0][40] = 1;
		(trpt+1)->bup.ovals = grab_ints(3);
		(trpt+1)->bup.ovals[0] = ((int)now.enq);
		now.enq = 0;
#ifdef VAR_RANGES
		logval("enq", ((int)now.enq));
#endif
		;
		/* merge: deq = 0(45, 41, 45) */
		reached[0][41] = 1;
		(trpt+1)->bup.ovals[1] = ((int)now.deq);
		now.deq = 0;
#ifdef VAR_RANGES
		logval("deq", ((int)now.deq));
#endif
		;
		/* merge: reject = 1(45, 42, 45) */
		reached[0][42] = 1;
		(trpt+1)->bup.ovals[2] = ((int)now.reject);
		now.reject = 1;
#ifdef VAR_RANGES
		logval("reject", ((int)now.reject));
#endif
		;
		_m = 3; goto P999; /* 2 */
	case 44: // STATE 44 - queue.pml:62 - [reject = 0] (0:0:1 - 1)
		IfNotBlocked
		reached[0][44] = 1;
		(trpt+1)->bup.oval = ((int)now.reject);
		now.reject = 0;
#ifdef VAR_RANGES
		logval("reject", ((int)now.reject));
#endif
		;
		_m = 3; goto P999; /* 0 */
	case 45: // STATE 49 - queue.pml:64 - [-end-] (0:0:0 - 1)
		IfNotBlocked
		reached[0][49] = 1;
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

