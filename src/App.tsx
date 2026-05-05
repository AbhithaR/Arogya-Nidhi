import React, { useState } from 'react';
import { Home, ClipboardList, Hospital as HospitalIcon, CheckSquare, Search, ChevronRight, ChevronLeft } from 'lucide-react';
import { SCHEMES, HOSPITALS, OCCUPATIONS, DISTRICTS, Scheme } from './data';

type Tab = 'quiz' | 'results' | 'hospitals';

interface QuizState {
  income: string;
  bpl: string;
  occupation: string;
  district: string;
}

export default function App() {
  const [activeTab, setActiveTab] = useState<Tab>('quiz');
  const [step, setStep] = useState(0);
  const [quizData, setQuizData] = useState<QuizState>({
    income: '',
    bpl: '',
    occupation: '',
    district: ''
  });
  const [eligibleSchemes, setEligibleSchemes] = useState<Scheme[]>([]);
  const [hospitalSearchTxt, setHospitalSearchTxt] = useState('');

  const calculateSchemes = (data: QuizState) => {
    const incomeNum = parseInt(data.income.replace(/\D/g, ''), 10) || 0;
    const isBpl = data.bpl === 'Yes';
    
    const matched = SCHEMES.filter(scheme => {
      let eligible = true;
      if (scheme.eligibility.bplRequired && !isBpl) {
        eligible = false;
      }
      if (scheme.eligibility.maxIncome && incomeNum > scheme.eligibility.maxIncome) {
        eligible = false;
      }
      if (scheme.eligibility.occupations && !scheme.eligibility.occupations.includes(data.occupation)) {
        eligible = false;
      }
      return eligible;
    });
    setEligibleSchemes(matched);
  };

  const handleNext = () => {
    if (step < 3) {
      setStep(step + 1);
    } else {
      calculateSchemes(quizData);
      setActiveTab('results');
    }
  };

  const handleUpdate = (field: keyof QuizState, value: string) => {
    setQuizData(prev => ({ ...prev, [field]: value }));
  };

  return (
    <div className="w-full min-h-screen bg-slate-50 text-slate-900 mx-auto overflow-hidden flex flex-col">
      {/* HEADER */}
      <header className="h-20 bg-white border-b border-slate-200 px-4 md:px-8 flex items-center justify-between flex-shrink-0">
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 bg-emerald-600 rounded-lg flex items-center justify-center text-white font-bold text-2xl">A</div>
          <div>
            <h1 className="text-xl font-bold text-slate-800 leading-none">Arogya-Nidhi</h1>
            <p className="text-xs text-slate-500 mt-1 uppercase tracking-wider font-semibold">Health Scheme Counselor</p>
          </div>
        </div>
        <div className="hidden md:flex items-center gap-6">
          <div className="flex items-center gap-2 text-sm font-medium text-slate-600 bg-slate-100 px-3 py-1.5 rounded-full border border-slate-200">
            <span className="w-2 h-2 bg-emerald-500 rounded-full"></span>Server Online
          </div>
          <div className="w-10 h-10 bg-slate-200 rounded-full border-2 border-white shadow-sm"></div>
        </div>
      </header>

      {/* NAVIGATION TABS */}
      <div className="px-4 md:px-8 py-4">
        <nav className="flex flex-wrap gap-4">
          <button 
            onClick={() => { setActiveTab('quiz'); setStep(0); }}
            className={`flex-1 flex items-center justify-center gap-2 py-3 px-4 border rounded-xl font-bold text-sm transition-all ${activeTab === 'quiz' ? 'bg-emerald-600 text-white border-emerald-600 shadow-md shadow-emerald-200' : 'bg-white text-slate-600 border-slate-200 hover:bg-slate-50'}`}
          >
            <Home size={18} /> New Scan
          </button>
          <button 
            onClick={() => setActiveTab('results')}
            className={`flex-1 flex items-center justify-center gap-2 py-3 px-4 border rounded-xl font-bold text-sm transition-all ${activeTab === 'results' ? 'bg-emerald-600 text-white border-emerald-600 shadow-md shadow-emerald-200' : 'bg-white text-slate-600 border-slate-200 hover:bg-slate-50'}`}
            disabled={eligibleSchemes.length === 0 && step !== 4}
          >
            <ClipboardList size={18} /> Results
          </button>
          <button 
            onClick={() => setActiveTab('hospitals')}
            className={`flex-1 flex items-center justify-center gap-2 py-3 px-4 border rounded-xl font-bold text-sm transition-all ${activeTab === 'hospitals' ? 'bg-emerald-600 text-white border-emerald-600 shadow-md shadow-emerald-200' : 'bg-white text-slate-600 border-slate-200 hover:bg-slate-50'}`}
          >
            <HospitalIcon size={18} /> Hospitals
          </button>
        </nav>
      </div>

      {/* MAIN CONTENT AREA */}
      <main className="flex-grow flex flex-col p-4 md:p-8 gap-6 overflow-hidden">
        {activeTab === 'quiz' && (
          <div className="bg-white rounded-2xl border border-slate-200 card-shadow p-6 md:p-8 flex-grow flex flex-col justify-between">
            <div className="flex items-center mb-8">
              {[0, 1, 2, 3].map((i) => (
                <React.Fragment key={i}>
                  <div className={`stepper-dot ${i < step ? 'completed-step' : i === step ? 'active-step' : 'pending-step'}`}>
                    {i < step ? '✓' : i + 1}
                  </div>
                  {i < 3 && <div className={`stepper-line ${i < step ? 'bg-emerald-200' : ''}`}></div>}
                </React.Fragment>
              ))}
            </div>

            <div className="min-h-[250px] flex-grow">
              {step === 0 && (
                <div className="space-y-4 max-w-xl mx-auto mt-6">
                  <h2 className="text-2xl font-bold text-slate-800">Select Your District</h2>
                  <p className="text-slate-500">Where does your family reside?</p>
                  <label className="block text-sm font-bold text-slate-700 mt-6 mb-2">District Name</label>
                  <select 
                    value={quizData.district}
                    onChange={(e) => handleUpdate('district', e.target.value)}
                    className="w-full px-4 py-3 bg-slate-50 border border-slate-200 rounded-xl focus:ring-2 focus:ring-emerald-500 focus:outline-none"
                  >
                    <option value="">-- SELECT AREA --</option>
                    {DISTRICTS.map(d => <option key={d} value={d}>{d}</option>)}
                  </select>
                </div>
              )}

              {step === 1 && (
                <div className="space-y-4 max-w-xl mx-auto mt-6">
                  <h2 className="text-2xl font-bold text-slate-800">Family Financial Profile</h2>
                  <p className="text-slate-500">This helps us determine eligibility for State and Central schemes.</p>
                  <label className="block text-sm font-bold text-slate-700 mt-6 mb-2">Monthly Family Income</label>
                  <div className="relative">
                    <span className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-400 font-medium">₹</span>
                    <input 
                      type="number"
                      value={quizData.income}
                      onChange={(e) => handleUpdate('income', e.target.value)}
                      placeholder="e.g. 15000"
                      className="w-full pl-8 pr-4 py-3 bg-slate-50 border border-slate-200 rounded-xl focus:ring-2 focus:ring-emerald-500 focus:outline-none"
                    />
                  </div>
                </div>
              )}

              {step === 2 && (
                <div className="space-y-4 max-w-xl mx-auto mt-6">
                  <h2 className="text-2xl font-bold text-slate-800">Below Poverty Line Status</h2>
                  <p className="text-slate-500">Does your family have a BPL (Below Poverty Line) card?</p>
                  <label className="block text-sm font-bold text-slate-700 mt-6 mb-2">BPL Card Holder?</label>
                  <div className="flex gap-4">
                    <button 
                      onClick={() => handleUpdate('bpl', 'Yes')}
                      className={`flex-1 py-3 border-2 font-bold rounded-xl transition-colors ${quizData.bpl === 'Yes' ? 'bg-emerald-50 border-emerald-600 text-emerald-700' : 'bg-slate-50 border-slate-200 text-slate-400 hover:bg-slate-100'}`}
                    >
                      Yes
                    </button>
                    <button 
                      onClick={() => handleUpdate('bpl', 'No')}
                      className={`flex-1 py-3 border-2 font-bold rounded-xl transition-colors ${quizData.bpl === 'No' ? 'bg-emerald-50 border-emerald-600 text-emerald-700' : 'bg-slate-50 border-slate-200 text-slate-400 hover:bg-slate-100'}`}
                    >
                      No
                    </button>
                  </div>
                </div>
              )}

              {step === 3 && (
                <div className="space-y-4 max-w-xl mx-auto mt-6">
                  <h2 className="text-2xl font-bold text-slate-800">Occupation Details</h2>
                  <p className="text-slate-500">What is the primary occupation of the breadwinner?</p>
                  <label className="block text-sm font-bold text-slate-700 mt-6 mb-2">Primary Occupation</label>
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    {OCCUPATIONS.map(occ => (
                      <button 
                        key={occ}
                        onClick={() => handleUpdate('occupation', occ)}
                        className={`p-3 border-2 text-sm font-semibold rounded-xl text-left transition-colors ${quizData.occupation === occ ? 'bg-emerald-50 border-emerald-600 text-emerald-700 shadow-sm' : 'bg-slate-50 border-slate-200 text-slate-600 hover:bg-slate-100'}`}
                      >
                        {occ}
                      </button>
                    ))}
                  </div>
                </div>
              )}
            </div>

            <div className="flex items-center justify-between pt-6 border-t border-slate-100 mt-8">
              <button 
                onClick={() => setStep(Math.max(0, step - 1))}
                disabled={step === 0}
                className="px-8 py-3 text-slate-500 font-bold hover:text-slate-700 disabled:opacity-30 disabled:hover:text-slate-500 flex items-center gap-2"
              >
                <ChevronLeft size={18} /> Back
              </button>
              <button 
                onClick={handleNext}
                disabled={(step === 0 && !quizData.district) || 
                          (step === 1 && !quizData.income) || 
                          (step === 2 && !quizData.bpl) || 
                          (step === 3 && !quizData.occupation)}
                className="px-10 py-3 bg-emerald-600 text-white font-bold rounded-xl shadow-lg shadow-emerald-200 hover:bg-emerald-700 transition-all disabled:opacity-50 flex items-center gap-2"
              >
                {step === 3 ? 'Analyze Data' : 'Continue'} {step !== 3 && <ChevronRight size={18} />}
              </button>
            </div>
          </div>
        )}

        {activeTab === 'results' && (
          <div className="bg-white rounded-2xl border border-slate-200 card-shadow p-6 md:p-8 flex-grow overflow-y-auto">
            <h2 className="text-2xl font-bold text-slate-800 flex items-center gap-2 mb-6 border-b border-slate-100 pb-4">
              <CheckSquare className="text-emerald-600" size={28} />
              Eligibility Results
            </h2>

            {eligibleSchemes.length > 0 ? (
              <div className="space-y-6">
                <p className="font-semibold text-slate-600">
                  Based on your profile, you are eligible for <span className="text-emerald-600 font-bold">{eligibleSchemes.length}</span> scheme(s).
                </p>
                <div className="grid gap-6 md:grid-cols-2">
                  {eligibleSchemes.map((scheme) => (
                    <div key={scheme.id} className="border border-slate-200 rounded-xl p-5 bg-slate-50 relative flex flex-col hover:border-emerald-200 transition-colors">
                      <div className="absolute top-4 right-4 text-emerald-600">
                        <CheckSquare size={20} />
                      </div>
                      <h3 className="font-bold text-lg text-slate-800 pr-8">{scheme.name}</h3>
                      <p className="text-sm text-slate-500 mt-2 flex-grow">{scheme.description}</p>
                      
                      <div className="mt-4 pt-4 border-t border-slate-200">
                        <h4 className="text-xs font-bold text-slate-700 uppercase tracking-wider mb-2">Required Documents</h4>
                        <ul className="text-sm space-y-1 text-slate-600">
                          {scheme.documents.map((doc, i) => (
                            <li key={i} className="flex gap-2 items-start">
                              <span className="text-emerald-500 mt-0.5">•</span> {doc}
                            </li>
                          ))}
                        </ul>
                      </div>
                    </div>
                  ))}
                </div>
                <div className="mt-8 pt-6 border-t border-slate-200 flex justify-end">
                  <button 
                    onClick={() => { setActiveTab('hospitals'); setHospitalSearchTxt(quizData.district); }}
                    className="px-6 py-3 bg-emerald-600 text-white font-bold rounded-xl shadow-md hover:bg-emerald-700 transition-all flex items-center gap-2"
                  >
                    <HospitalIcon size={18} /> View Eligible Hospitals
                  </button>
                </div>
              </div>
            ) : (
              <div className="text-center py-12 rounded-xl bg-slate-50 border border-slate-200">
                <p className="font-bold text-slate-800 mb-2">No Matches Found</p>
                <p className="text-slate-500 max-w-md mx-auto">Based on the provided information, we couldn't match you with any active schemes in our database.</p>
                <button 
                  onClick={() => { setActiveTab('quiz'); setStep(0); }}
                  className="mt-6 px-6 py-2 border border-slate-300 bg-white text-slate-700 font-bold rounded-xl hover:bg-slate-50 shadow-sm transition-colors"
                >
                  Update Profile
                </button>
              </div>
            )}
          </div>
        )}

        {activeTab === 'hospitals' && (
          <div className="bg-slate-900 rounded-2xl p-6 md:p-8 text-white card-shadow flex-grow flex flex-col min-h-[400px]">
            <h3 className="font-bold mb-6 flex items-center justify-between text-xl">
              <span className="flex items-center gap-2">
                <HospitalIcon className="text-emerald-500" size={24} /> 
                Nearby Hospitals
              </span>
              <span className="text-xs bg-emerald-500 text-white px-3 py-1 rounded-full font-semibold">
                {HOSPITALS.filter(h => (!hospitalSearchTxt || h.district.toLowerCase().includes(hospitalSearchTxt.toLowerCase()))).length} Found
              </span>
            </h3>
            
            <div className="relative mb-6">
              <input 
                type="text" 
                value={hospitalSearchTxt}
                onChange={(e) => setHospitalSearchTxt(e.target.value)}
                placeholder="Search District..."
                className="w-full bg-slate-800 border-none rounded-xl py-3 px-10 text-sm focus:ring-2 focus:ring-emerald-500 focus:outline-none"
              />
              <Search className="w-5 h-5 absolute left-3 top-3 text-slate-500" />
            </div>

            <div className="space-y-4 overflow-y-auto flex-grow pb-4 pr-2">
              {HOSPITALS
                .filter(h => (!hospitalSearchTxt || h.district.toLowerCase().includes(hospitalSearchTxt.toLowerCase())))
                .map((hospital) => {
                  const supportedEligible = eligibleSchemes.filter(s => hospital.schemes.includes(s.id));
                  
                  return (
                  <div key={hospital.id} className="border-b border-slate-800 pb-4 last:border-0 last:pb-0">
                    <div className="flex justify-between items-start mb-1">
                      <p className="font-semibold text-lg">{hospital.name}</p>
                      <span className={`text-[10px] px-2 py-0.5 rounded font-bold uppercase tracking-wider ${hospital.type === 'Private' ? 'bg-blue-900/50 text-blue-400 border border-blue-800' : 'bg-emerald-900/50 text-emerald-400 border border-emerald-800'}`}>
                        {hospital.type}
                      </span>
                    </div>
                    <p className="text-sm text-slate-400 flex items-center gap-1">
                      <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"></path><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"></path></svg>
                      {hospital.district}
                    </p>
                    
                    {supportedEligible.length > 0 ? (
                      <div className="flex flex-wrap gap-2 mt-3">
                        {supportedEligible.map(s => (
                          <span key={s.id} className="text-[10px] bg-slate-800 text-emerald-400 px-2 py-1 rounded border border-slate-700">
                            {s.name.split(' ')[0]} {/* Shorten name for tag */}
                          </span>
                        ))}
                      </div>
                    ) : (
                       <div className="mt-2">
                         <p className="text-slate-600 text-xs">Total Schemes Supported: {hospital.schemes.length}</p>
                       </div>
                    )}
                  </div>
                )})}
                
              {HOSPITALS.filter(h => (!hospitalSearchTxt || h.district.toLowerCase().includes(hospitalSearchTxt.toLowerCase()))).length === 0 && (
                <div className="py-12 text-center">
                  <p className="text-slate-500 font-semibold">No hospitals found in this sector</p>
                </div>
              )}
            </div>
          </div>
        )}
      </main>

      {/* FOOTER */}
      <footer className="h-12 bg-emerald-700 text-emerald-100 flex items-center px-4 md:px-8 text-xs font-medium justify-between flex-shrink-0">
        <div className="flex gap-4 md:gap-6">
          <span>&copy; 2024 Arogya-Nidhi Initiative</span>
          <span className="hidden md:inline">Privacy Policy</span>
        </div>
        <div className="flex items-center gap-2 font-bold">
          <div className="w-2 h-2 bg-emerald-300 rounded-full animate-pulse"></div>
          Encrypted & Secure
        </div>
      </footer>
    </div>
  );
}
