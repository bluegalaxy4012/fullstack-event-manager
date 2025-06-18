import './App.css';
import React, {useState, useEffect, useCallback} from 'react';

const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api/probe';
const WS_URL = process.env.REACT_APP_WS_URL || 'ws://localhost:8080/ws/probe';

function App() {
  const [probe, setProbe] = useState([]);
  const [inputData, setInputData] = useState({ distanta: '', stil: '' });
  const [editActive, setEditActive] = useState(false);
  const [eroare, setEroare] = useState('');
  const [isLoading, setIsLoading] = useState(false);


  const fetchProbe = useCallback(async () => {
    setIsLoading(true);
    try
    {
      const rez = await fetch(API_URL);
      if (!rez.ok) throw new Error('Failed to fetch api url');

      const data = await rez.json();
      setProbe(data);
      setEroare('');
    }
    catch (err)
    {
      setEroare('Failed to fetch probe: ' + err.message);
    }
    finally
    {
      setIsLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchProbe();

    const websocket = new WebSocket(WS_URL);

    websocket.onmessage = (e) => {
      console.log('Message received(websocket): ', e.data);

      const [actiune, data] = e.data.split(': ');
      const proba = JSON.parse(data);

      switch (actiune) {
        case 'Proba added':
          setProbe((prevProbe) => [...prevProbe, proba]);
          break;
        case 'Proba updated':
          setProbe((prevProbe) => prevProbe.map(p => p.id === proba.id ? proba : p));
          break;
        case 'Proba deleted':
          setProbe((prevProbe) => prevProbe.filter(p => p.id !== proba.id));
          break;
        default:
          console.warn('Unknown action:', actiune);
          break;
      }

    };

    return () => {
      websocket.close();
    }

  }, [fetchProbe] );




  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);


    try
    {
      const PUTorPOST = editActive ? 'PUT' : 'POST';
      const url = editActive ? `${API_URL}/${inputData.id}` : API_URL;

      const rez = await fetch(url, {
        method: PUTorPOST,
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(inputData),
      });

      if (!rez.ok) throw new Error('Failed to save proba');


      setInputData({ distanta: '', stil: '' });
      setEditActive(false);
      setEroare('');
    }
    catch (err)
    {
      setEroare(err.message);
    }
    finally
    {
      setIsLoading(false);
    }

  };

  const handleInput = (e) => {
    setInputData({
      ...inputData,
      [e.target.name]: e.target.value,
    });
  };

  const handleEdit = (proba) => {
    setInputData({...proba });
    setEditActive(true);
  };


  const handleDelete = async (id) => {
    setIsLoading(true);
    try
    {
      const rez = await fetch(`${API_URL}/${id}`, {
        method: 'DELETE',
      });

      if (!rez.ok) throw new Error('Failed to delete proba');

      setProbe((prev) => prev.filter((p) => p.id !== id));
      setEroare('');
    }
    catch (err)
    {
      setEroare(err.message);
    }
    finally
    {
      setIsLoading(false);
    }

  };


  const resetInput = () => {
    setInputData({ distanta: '', stil: '' });
    setEditActive(false);
  };





  return (
      <div className="App">
        <div className="animated-background">
          <div className="floating-shapes">
            <div className="shape shape-1"></div>
            <div className="shape shape-2"></div>
            <div className="shape shape-3"></div>
            <div className="shape shape-4"></div>
            <div className="shape shape-5"></div>
          </div>
        </div>

        {(() => {
          if (eroare !== '') {
            return <div className="error">{eroare}</div>;
          } else {
            return null;
          }
        })()}

        {(() => {
          if (isLoading) {
            return <div className="spinner">Loading...</div>;
          } else {
            return null;
          }
        })()}

        <div className="form-container">
          <h2 className="form-title">{editActive ? 'Update proba' : 'Add proba'}</h2>
          <form onSubmit={handleSubmit} className="modern-form">


            <div className="input-group">
              <label>Distanta:</label>
              <input
                  type="text"
                  name="distanta"
                  value={inputData.distanta}
                  onChange={handleInput}
                  required
                  className="modern-input"
              />
            </div>


            <div className="input-group">
              <label>Stil:</label>
              <input
                  type="text"
                  name="stil"
                  value={inputData.stil}
                  onChange={handleInput}
                  required
                  className="modern-input"
              />
            </div>



            <div className="button-group">
              <button type="submit" className="btn btn-primary">{editActive ? 'Update' : 'Add'}</button>

              {(() => {
                    if (editActive) {
                      return (
                          <button type="button" onClick={resetInput} className="btn btn-secondary">
                            Cancel
                          </button>
                      );
                    }
                  }
              )()}
            </div>


          </form>
        </div>



        <div className="table-container">
          <h2 className="table-title">Probe</h2>
          <div className="table-wrapper">
            <table className="modern-table">
              <thead>
              <tr>
                <th>ID</th>
                <th>Distanta</th>
                <th>Stil</th>
                <th>Actions</th>
              </tr>
              </thead>

              <tbody>
              {probe.length === 0 ? (
                  <tr>
                    <td colSpan="4" className="no-data">No probe available</td>
                  </tr>
              ) : (
                  probe.map(function (proba) {

                    return (
                        <tr key={proba.id} className="table-row">
                          <td>{proba.id}</td>
                          <td>{proba.distanta}</td>
                          <td>{proba.stil}</td>
                          <td className="action-buttons">
                            <button onClick={function () { handleEdit(proba); }} className="btn btn-edit">
                              Update
                            </button>

                            <button onClick={function () { handleDelete(proba.id); }} className="btn btn-delete">
                              Delete
                            </button>
                          </td>
                        </tr>
                    );

                  })
              )
              }
              </tbody>

            </table>
          </div>
        </div>
      </div>
  );

}

export default App;
