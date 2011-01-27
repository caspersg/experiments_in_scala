/*
Copyright 2010 naedyr@gmail.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0
       
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package naedyrscala.experiments

// from http://www.martinfowler.com/bliki/SyntacticNoise.html

/*Event doorClosed = new Event("doorClosed", "D1CL"); 
Event drawOpened = new Event("drawOpened", "D2OP"); 
Event lightOn = new Event("lightOn", "L1ON"); 
 
Command lockPanelCmd = new Command("lockPanel", "PNLK"); 
Command unlockDoorCmd = new Command("unlockDoor", "D1UL"); 

State idle = new State("idle"); 
State activeState = new State("active"); 
 
StateMachine machine = new StateMachine(idle); 

idle.addTransition(doorClosed, activeState);
idle.addCommand(unlockDoorCmd);
idle.addCommand(lockPanelCmd);

activeState.addTransition(drawOpened, waitingForLightState);
activeState.addTransition(lightOn, waitingForDrawState);*/

case class Event(name: Symbol, id: Symbol)
case class Command(name: Symbol, id: Symbol)
case class State(name: Symbol) {
  def +(cmd: Command): State = this
  def +(transition: (Event, State)): State = this
}
case class StateMachine(var state: State)
class BasicStateMachine {
  val machine = StateMachine(State('idle) +
    Command('unlockDoor, 'D1UL) +
    Command('lockPanel, 'PNLK) + 
        (Event('doorClosed, 'D1CL) ->
            (State('active) + 
                    (Event('drawOpened, 'D2OP) ->
                        State('waitingForLightState)) +
                    (Event('lightOn, 'L1ON) ->
                        State('waitingForDrawState)))))
}