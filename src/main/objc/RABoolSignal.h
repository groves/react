//
// React - a library for functional-reactive-like programming
// Copyright (c) 2011, Three Rings Design, Inc. - All rights reserved.
// http://github.com/threerings/react/blob/master/LICENSE

#import <Foundation/Foundation.h>
#import "RAReactor.h"

typedef void (^RABoolSlot)(BOOL);

@interface RABoolSignal : RAReactor
/** @name Emission */

/** Emits the supplied value to all connected slots. */
- (void) emitEvent:(BOOL)event;

/** @name Connection */

/** Connects the given block to receieve emissions from this signal at the default priority.  */
- (RAConnection*) connectSlot:(RABoolSlot)block;

/** Connects the given block at the given priority.  */
- (RAConnection*) withPriority:(int)priority connectSlot:(RABoolSlot)block;

/** Connects the given unit at the default priority.  */
- (RAConnection*) connectUnit:(RAUnitBlock)block;

/** Connects the given unit at the given priority.  */
- (RAConnection*) withPriority:(int)priority connectUnit:(RAUnitBlock)block;
@end